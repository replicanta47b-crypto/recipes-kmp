package com.example.restapi.rating

import com.example.restapi.messaging.RecipeRatedMessage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RatingServiceTest {

    private val service = RatingService()

    @Test
    fun averageOfSeveralUsers() {
        service.apply(RecipeRatedMessage(recipeId = 1, userId = 1, rating = 5))
        val result = service.apply(RecipeRatedMessage(recipeId = 1, userId = 2, rating = 2))
        assertEquals(3.5, result.average)
        assertEquals(2, result.votes)
    }

    @Test
    fun userCanChangeTheVoteButNotVoteTwice() {
        service.apply(RecipeRatedMessage(1, 1, 1))
        val result = service.apply(RecipeRatedMessage(1, 1, 5))
        assertEquals(5.0, result.average)
        assertEquals(1, result.votes)
    }

    @Test
    fun invalidRatingIsRejected() {
        assertFailsWith<IllegalArgumentException> { service.apply(RecipeRatedMessage(1, 1, 6)) }
        assertFailsWith<IllegalArgumentException> { service.apply(RecipeRatedMessage(1, 1, 0)) }
        assertFailsWith<IllegalArgumentException> { service.apply(RecipeRatedMessage(0, 1, 3)) }
    }
}
