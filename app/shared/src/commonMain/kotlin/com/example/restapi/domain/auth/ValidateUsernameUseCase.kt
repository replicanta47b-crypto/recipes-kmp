package com.example.restapi.domain.auth

internal enum class UsernameError { Empty, TooShort, TooLong, InvalidCharacters }

/** Returns the first rule the [username] breaks, or `null` when it is valid. */
internal class ValidateUsernameUseCase {

    operator fun invoke(username: String): UsernameError? = when {
        username.isBlank() -> UsernameError.Empty
        username.length < MIN_LENGTH -> UsernameError.TooShort
        username.length > MAX_LENGTH -> UsernameError.TooLong
        !username.all { it.isLetterOrDigit() || it in ALLOWED_SYMBOLS } -> UsernameError.InvalidCharacters
        else -> null
    }

    companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 30
        private const val ALLOWED_SYMBOLS = "._-"
    }
}
