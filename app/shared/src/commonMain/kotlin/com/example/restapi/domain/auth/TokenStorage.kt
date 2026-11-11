package com.example.restapi.domain.auth

/** Secure place for the tokens. Implemented with encryption on every platform. */
internal interface TokenStorage {
    suspend fun getTokens(): AuthTokens?
    suspend fun saveTokens(tokens: AuthTokens)
    suspend fun clear()
}
