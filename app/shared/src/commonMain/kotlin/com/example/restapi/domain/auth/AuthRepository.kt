package com.example.restapi.domain.auth

import com.example.restapi.domain.common.AppResult

internal interface AuthRepository {
    /** Signs in, stores the received tokens securely and returns the signed-in user. */
    suspend fun login(username: String, password: String): AppResult<User>

    /** Loads the profile of the current user (a request that needs the access token). */
    suspend fun getCurrentUser(): AppResult<User>

    /** True when tokens of a previous session are stored on the device. */
    suspend fun isAuthorized(): Boolean

    suspend fun logout()
}
