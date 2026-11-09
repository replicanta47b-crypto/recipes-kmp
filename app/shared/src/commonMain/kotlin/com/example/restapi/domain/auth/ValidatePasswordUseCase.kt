package com.example.restapi.domain.auth

internal enum class PasswordError { Empty, TooShort, TooLong }

/** Returns the first rule the [password] breaks, or `null` when it is valid. */
internal class ValidatePasswordUseCase {

    operator fun invoke(password: String): PasswordError? = when {
        password.isBlank() -> PasswordError.Empty
        password.length < MIN_LENGTH -> PasswordError.TooShort
        password.length > MAX_LENGTH -> PasswordError.TooLong
        else -> null
    }

    companion object {
        const val MIN_LENGTH = 6
        const val MAX_LENGTH = 64
    }
}
