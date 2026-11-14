package com.example.restapi.messaging

/** Connection settings; every value can be overridden by an environment variable. */
data class RabbitConfig(
    val host: String = System.getenv("RABBITMQ_HOST") ?: "localhost",
    val port: Int = System.getenv("RABBITMQ_PORT")?.toIntOrNull() ?: 5672,
    val username: String = System.getenv("RABBITMQ_USER") ?: "guest",
    val password: String = System.getenv("RABBITMQ_PASSWORD") ?: "guest",
)
