package com.example.restapi.rating

import com.example.restapi.messaging.Queues
import com.example.restapi.messaging.RabbitMessaging
import com.example.restapi.messaging.RatingUpdatedMessage
import com.example.restapi.messaging.RecipeRatedMessage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

/**
 * The business side of the incoming messages:
 * 1. a "recipe.rated" message is parsed and applied to the ratings;
 * 2. another asynchronous message "recipe.rating.updated" is sent with the new average;
 * 3. a second consumer of that queue reacts to it (here: logs a notification).
 */
class RatingMessageHandler(
    private val messaging: RabbitMessaging,
    private val service: RatingService,
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    private val log = LoggerFactory.getLogger(RatingMessageHandler::class.java)

    fun start() {
        messaging.consume(Queues.RECIPE_RATED, ::onRecipeRated)
        messaging.consume(Queues.RATING_UPDATED, ::onRatingUpdated)
    }

    internal fun onRecipeRated(body: String) {
        val message = json.decodeFromString<RecipeRatedMessage>(body)
        log.info("Received {}", message)
        val updated = service.apply(message)
        messaging.publish(Queues.RATING_UPDATED, json.encodeToString(updated))
    }

    private fun onRatingUpdated(body: String) {
        val message = json.decodeFromString<RatingUpdatedMessage>(body)
        log.info("Recipe {} now has rating {} from {} vote(s)", message.recipeId, "%.2f".format(message.average), message.votes)
    }
}
