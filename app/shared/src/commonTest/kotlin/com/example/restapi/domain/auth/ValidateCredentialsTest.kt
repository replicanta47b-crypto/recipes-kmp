package com.example.restapi.domain.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ValidateCredentialsTest {

    private val validateUsername = ValidateUsernameUseCase()
    private val validatePassword = ValidatePasswordUseCase()

    @Test
    fun usernameIsRequired() {
        assertEquals(UsernameError.Empty, validateUsername(""))
        assertEquals(UsernameError.Empty, validateUsername("   "))
    }

    @Test
    fun usernameLengthIsLimited() {
        assertEquals(UsernameError.TooShort, validateUsername("ab"))
        assertEquals(UsernameError.TooLong, validateUsername("a".repeat(31)))
        assertNull(validateUsername("abc"))
        assertNull(validateUsername("a".repeat(30)))
    }

    @Test
    fun usernameAllowsOnlyLettersDigitsAndSomeSymbols() {
        assertEquals(UsernameError.InvalidCharacters, validateUsername("john doe"))
        assertEquals(UsernameError.InvalidCharacters, validateUsername("john@doe"))
        assertNull(validateUsername("john.doe_1-x"))
        assertNull(validateUsername("артем"))
    }

    @Test
    fun passwordIsRequired() {
        assertEquals(PasswordError.Empty, validatePassword(""))
        assertEquals(PasswordError.Empty, validatePassword("      "))
    }

    @Test
    fun passwordLengthIsLimited() {
        assertEquals(PasswordError.TooShort, validatePassword("12345"))
        assertEquals(PasswordError.TooLong, validatePassword("a".repeat(65)))
        assertNull(validatePassword("123456"))
        assertNull(validatePassword("emilyspass"))
    }
}
