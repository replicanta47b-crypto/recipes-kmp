package com.example.restapi.data.auth.model

import com.example.restapi.domain.auth.User

internal fun UserDto.toDomain() = User(id, username, firstName, lastName, email)

internal fun LoginResponseDto.toUser() = User(id, username, firstName, lastName, email)
