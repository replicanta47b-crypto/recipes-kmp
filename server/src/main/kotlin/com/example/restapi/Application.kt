package com.example.restapi

import com.example.restapi.messaging.Queues
import com.example.restapi.messaging.RabbitMessaging
import com.example.restapi.messaging.RecipeRatedMessage
import com.example.restapi.rating.RatingMessageHandler
import com.example.restapi.rating.RatingService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@Serializable
private data class RateRequest(val userId: Int, val rating: Int)

/**
 * @param messaging the RabbitMQ connection; `null` runs the server without a broker (tests).
 */
fun Application.module(messaging: RabbitMessaging? = RabbitMessaging()) {
    val log = LoggerFactory.getLogger("Application")
    val ratings = RatingService()
    val json = Json { ignoreUnknownKeys = true }

    install(ContentNegotiation) { json() }

    if (messaging != null) {
        // The broker may start later than the server: keep trying in the background, the HTTP API works meanwhile.
        val connector = thread(name = "rabbit-connector", isDaemon = true) {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    messaging.connect()
                    RatingMessageHandler(messaging, ratings).start()
                    break
                } catch (e: InterruptedException) {
                    break
                } catch (e: Exception) {
                    log.warn("RabbitMQ is not available ({}), retry in 5 s", e.message)
                    try { Thread.sleep(5_000) } catch (_: InterruptedException) { break }
                }
            }
        }
        monitor.subscribe(ApplicationStopping) {
            connector.interrupt()
            messaging.close()
        }
    }

    routing {
        get("/") {
            call.respondText(sayHello("Ktor"))
        }

        // Does not change anything by itself: it only sends a message, the consumer does the work later.
        post("/recipes/{id}/ratings") {
            val recipeId = call.parameters["id"]?.toIntOrNull()
            if (recipeId == null || messaging == null || !messaging.isConnected) {
                call.respond(HttpStatusCode.ServiceUnavailable, "Message broker is not available")
                return@post
            }
            val request = call.receive<RateRequest>()
            messaging.publish(
                Queues.RECIPE_RATED,
                json.encodeToString(RecipeRatedMessage(recipeId, request.userId, request.rating)),
            )
            call.respond(HttpStatusCode.Accepted)
        }

        get("/recipes/{id}/rating") {
            val recipeId = call.parameters["id"]?.toIntOrNull()
            if (recipeId == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                call.respond(ratings.summary(recipeId))
            }
        }
    }
}
