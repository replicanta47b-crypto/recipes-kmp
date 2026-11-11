package com.example.restapi.domain.auth

internal data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
)

internal data class User(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
) {
    val displayName: String get() = "$firstName $lastName".trim().ifEmpty { username }
}
