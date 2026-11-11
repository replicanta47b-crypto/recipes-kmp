package com.example.restapi.domain.common

import kotlin.test.Test
import kotlin.test.assertEquals

class AppResultTest {

    @Test
    fun mapTransformsSuccess() {
        val result: AppResult<Int> = AppResult.Success(2)
        assertEquals(AppResult.Success(4), result.map { it * 2 })
    }

    @Test
    fun mapKeepsFailure() {
        val result: AppResult<Int> = AppResult.Failure(AppError.Network)
        assertEquals(AppResult.Failure(AppError.Network), result.map { it * 2 })
    }

    @Test
    fun onSuccessAndOnFailureRunOnlyForTheirCase() {
        var success = 0
        var failure = 0
        AppResult.Success(1).onSuccess { success++ }.onFailure { failure++ }
        AppResult.Failure(AppError.Timeout).onSuccess { success++ }.onFailure { failure++ }
        assertEquals(1, success)
        assertEquals(1, failure)
    }
}
