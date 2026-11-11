package com.example.restapi.ui.auth

import com.example.restapi.domain.auth.AuthRepository
import com.example.restapi.domain.auth.LoginUseCase
import com.example.restapi.domain.auth.PasswordError
import com.example.restapi.domain.auth.User
import com.example.restapi.domain.auth.UsernameError
import com.example.restapi.domain.auth.ValidatePasswordUseCase
import com.example.restapi.domain.auth.ValidateUsernameUseCase
import com.example.restapi.domain.common.AppError
import com.example.restapi.domain.common.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private class FakeAuthRepository(var result: AppResult<User>) : AuthRepository {
        var loginCalls = 0
        var lastUsername: String? = null

        override suspend fun login(username: String, password: String): AppResult<User> {
            loginCalls++
            lastUsername = username
            return result
        }

        override suspend fun getCurrentUser(): AppResult<User> = result
        override suspend fun isAuthorized(): Boolean = false
        override suspend fun logout() = Unit
    }

    private val user = User(1, "emilys", "Emily", "Johnson", "emily@example.com")

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel(repository: FakeAuthRepository) = AuthViewModel(
        validateUsername = ValidateUsernameUseCase(),
        validatePassword = ValidatePasswordUseCase(),
        login = LoginUseCase(repository),
    )

    @Test
    fun invalidInputDoesNotCallTheServer() {
        val repository = FakeAuthRepository(AppResult.Success(user))
        val vm = viewModel(repository)

        vm.onAction(AuthAction.SignIn)

        assertEquals(0, repository.loginCalls)
        assertEquals(UsernameError.Empty, vm.state.value.usernameError)
        assertEquals(PasswordError.Empty, vm.state.value.passwordError)
    }

    @Test
    fun successfulLoginSendsSignedInEvent() = runTest {
        val repository = FakeAuthRepository(AppResult.Success(user))
        val vm = viewModel(repository)

        vm.onAction(AuthAction.UsernameChanged("  emilys "))
        vm.onAction(AuthAction.PasswordChanged("emilyspass"))
        vm.onAction(AuthAction.SignIn)

        assertEquals(AuthEvent.SignedIn, vm.events.first())
        assertEquals("emilys", repository.lastUsername) // the username is trimmed
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun failedLoginSendsErrorEventAndUnlocksTheForm() = runTest {
        val repository = FakeAuthRepository(AppResult.Failure(AppError.InvalidCredentials))
        val vm = viewModel(repository)

        vm.onAction(AuthAction.UsernameChanged("emilys"))
        vm.onAction(AuthAction.PasswordChanged("wrong-pass"))
        vm.onAction(AuthAction.SignIn)

        assertEquals(AuthEvent.ShowError(AppError.InvalidCredentials), vm.events.first())
        assertFalse(vm.state.value.isLoading)
    }
}
