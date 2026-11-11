package com.example.restapi.domain.auth

import com.example.restapi.domain.common.AppResult

internal class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): AppResult<User> =
        repository.login(username.trim(), password)
}

internal class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): AppResult<User> = repository.getCurrentUser()
}

internal class IsAuthorizedUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Boolean = repository.isAuthorized()
}

internal class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}
