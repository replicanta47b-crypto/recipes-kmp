package com.example.restapi.messaging

import kotlinx.serialization.Serializable

/** Names of the RabbitMQ queues used by the server. */
object Queues {
    /** Incoming: somebody has rated a recipe. */
    const val RECIPE_RATED = "recipe.rated"

    /** Outgoing: the aggregated rating of a recipe has changed. */
    const val RATING_UPDATED = "recipe.rating.updated"
}

/** Message received from [Queues.RECIPE_RATED]. */
@Serializable
data class RecipeRatedMessage(
    val recipeId: Int,
    val userId: Int,
    val rating: Int,
)

/** Message sent to [Queues.RATING_UPDATED] after a rating has been processed. */
@Serializable
data class RatingUpdatedMessage(
    val recipeId: Int,
    val average: Double,
    val votes: Int,
)
