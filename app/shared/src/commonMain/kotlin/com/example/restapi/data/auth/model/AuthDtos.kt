package com.example.restapi.data.auth.model

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequestDto(
    val username: String,
    val password: String,
    val expiresInMins: Int,
)

/** dummyjson returns the profile together with the tokens; unknown fields are ignored. */
@Serializable
internal data class LoginResponseDto(
    val id: Int,
    val username: String,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val accessToken: String,
    val refreshToken: String,
)

@Serializable
internal data class RefreshRequestDto(
    val refreshToken: String,
    val expiresInMins: Int,
)

@Serializable
internal data class RefreshResponseDto(
    val accessToken: String,
    val refreshToken: String,
)

@Serializable
internal data class UserDto(
    val id: Int,
    val username: String,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
)
