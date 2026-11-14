package com.example.restapi.messaging

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.MessageProperties
import org.slf4j.LoggerFactory

/**
 * Thin wrapper over the RabbitMQ Java client: durable queues, manual acknowledgements,
 * automatic recovery of the connection after a network failure.
 */
class RabbitMessaging(private val config: RabbitConfig = RabbitConfig()) : AutoCloseable {

    private val log = LoggerFactory.getLogger(RabbitMessaging::class.java)

    private var connection: Connection? = null
    private var consumerChannel: Channel? = null
    private var publisherChannel: Channel? = null

    val isConnected: Boolean get() = connection?.isOpen == true

    /** Opens the connection. Throws when the broker is not reachable. */
    @Synchronized
    fun connect() {
        if (isConnected) return
        val factory = ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.username
            password = config.password
            isAutomaticRecoveryEnabled = true
            setNetworkRecoveryInterval(5_000)
        }
        val newConnection = factory.newConnection("recipe-server")
        connection = newConnection
        // A channel must not be shared between threads, so consuming and publishing use their own.
        consumerChannel = newConnection.createChannel().apply { basicQos(10) }
        publisherChannel = newConnection.createChannel()
        listOf(Queues.RECIPE_RATED, Queues.RATING_UPDATED).forEach { declareQueue(it) }
        log.info("Connected to RabbitMQ at {}:{}", config.host, config.port)
    }

    private fun declareQueue(queue: String) {
        // durable = true: the queue survives a restart of the broker.
        consumerChannel!!.queueDeclare(queue, true, false, false, null)
    }

    /**
     * Subscribes [handler] to [queue]. The message is acknowledged when the handler returns and
     * rejected (not requeued) when it throws, so a broken message cannot block the queue forever.
     */
    fun consume(queue: String, handler: (String) -> Unit) {
        val channel = consumerChannel ?: error("Not connected")
        val onMessage = DeliverCallback { _, delivery ->
            val tag = delivery.envelope.deliveryTag
            try {
                handler(String(delivery.body, Charsets.UTF_8))
                channel.basicAck(tag, false)
            } catch (e: Exception) {
                log.warn("Message from '{}' rejected: {}", queue, e.message)
                channel.basicNack(tag, false, false)
            }
        }
        channel.basicConsume(queue, false, onMessage, CancelCallback { log.warn("Consumer of '{}' cancelled", queue) })
        log.info("Listening to queue '{}'", queue)
    }

    @Synchronized
    fun publish(queue: String, body: String) {
        val channel = publisherChannel ?: error("Not connected")
        channel.basicPublish("", queue, MessageProperties.PERSISTENT_TEXT_PLAIN, body.toByteArray(Charsets.UTF_8))
    }

    override fun close() {
        runCatching { consumerChannel?.close() }
        runCatching { publisherChannel?.close() }
        runCatching { connection?.close() }
    }
}
