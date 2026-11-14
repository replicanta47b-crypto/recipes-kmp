package com.example.restapi.rating

import com.example.restapi.messaging.RatingUpdatedMessage
import com.example.restapi.messaging.RecipeRatedMessage
import java.util.concurrent.ConcurrentHashMap

/** Ratings of one recipe: the latest vote of every user (a user can change the vote, not add another). */
class RatingService {

    private val votes = ConcurrentHashMap<Int, ConcurrentHashMap<Int, Int>>()

    /**
     * Stores the rating from the message and returns the new aggregate.
     * @throws IllegalArgumentException when the message is invalid.
     */
    fun apply(message: RecipeRatedMessage): RatingUpdatedMessage {
        require(message.rating in MIN_RATING..MAX_RATING) { "rating must be $MIN_RATING..$MAX_RATING" }
        require(message.recipeId > 0 && message.userId > 0) { "ids must be positive" }
        votes.computeIfAbsent(message.recipeId) { ConcurrentHashMap() }[message.userId] = message.rating
        return summary(message.recipeId)
    }

    fun summary(recipeId: Int): RatingUpdatedMessage {
        val ratings = votes[recipeId]?.values.orEmpty()
        val average = if (ratings.isEmpty()) 0.0 else ratings.sum().toDouble() / ratings.size
        return RatingUpdatedMessage(recipeId, average, ratings.size)
    }

    companion object {
        const val MIN_RATING = 1
        const val MAX_RATING = 5
    }
}
