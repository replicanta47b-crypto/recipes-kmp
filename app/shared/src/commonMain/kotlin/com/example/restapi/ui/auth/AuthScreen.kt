package com.example.restapi.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.restapi.domain.auth.PasswordError
import com.example.restapi.domain.auth.UsernameError
import com.example.restapi.ui.common.ObserveEvents
import com.example.restapi.ui.common.message
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.auth_hide_password
import restapi.app.shared.generated.resources.auth_password_error_empty
import restapi.app.shared.generated.resources.auth_password_error_too_long
import restapi.app.shared.generated.resources.auth_password_error_too_short
import restapi.app.shared.generated.resources.auth_password_label
import restapi.app.shared.generated.resources.auth_show_password
import restapi.app.shared.generated.resources.auth_sign_in
import restapi.app.shared.generated.resources.auth_subtitle
import restapi.app.shared.generated.resources.auth_title
import restapi.app.shared.generated.resources.auth_username_error_empty
import restapi.app.shared.generated.resources.auth_username_error_invalid_characters
import restapi.app.shared.generated.resources.auth_username_error_too_long
import restapi.app.shared.generated.resources.auth_username_error_too_short
import restapi.app.shared.generated.resources.auth_username_label
import restapi.app.shared.generated.resources.ic_restaurant

@Composable
internal fun AuthRoute(
    onSignedIn: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            AuthEvent.SignedIn -> onSignedIn()
            is AuthEvent.ShowError -> scope.launch {
                // A new error replaces the one still on the screen instead of waiting in a queue.
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(getString(event.error.message()))
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        AuthScreen(state = state, onAction = viewModel::onAction)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).safeContentPadding().imePadding(),
        )
    }
}

@Composable
internal fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    // The "Sign in" button and the keyboard action of the password field do exactly the same.
    val signIn = {
        keyboard?.hide()
        focusManager.clearFocus()
        onAction(AuthAction.SignIn)
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        errorContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = Color.Transparent,
        errorBorderColor = MaterialTheme.colorScheme.error,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_restaurant),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(Res.string.auth_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.auth_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(32.dp))

        // Username: the keyboard action moves the focus to the password field.
        OutlinedTextField(
            value = state.username,
            onValueChange = { onAction(AuthAction.UsernameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.auth_username_label)) },
            singleLine = true,
            enabled = !state.isLoading,
            isError = state.usernameError != null,
            supportingText = state.usernameError?.let { { Text(stringResource(it.message())) } },
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) }),
        )
        Spacer(Modifier.height(12.dp))

        // Password: the keyboard action signs the user in, as the button does.
        OutlinedTextField(
            value = state.password,
            onValueChange = { onAction(AuthAction.PasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.auth_password_label)) },
            singleLine = true,
            enabled = !state.isLoading,
            isError = state.passwordError != null,
            supportingText = state.passwordError?.let { { Text(stringResource(it.message())) } },
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors,
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { onAction(AuthAction.TogglePasswordVisibility) }) {
                    Text(
                        stringResource(
                            if (state.isPasswordVisible) Res.string.auth_hide_password else Res.string.auth_show_password
                        ),
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { signIn() }),
        )
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { signIn() },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text(
                    text = stringResource(Res.string.auth_sign_in),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

private fun UsernameError.message(): StringResource = when (this) {
    UsernameError.Empty -> Res.string.auth_username_error_empty
    UsernameError.TooShort -> Res.string.auth_username_error_too_short
    UsernameError.TooLong -> Res.string.auth_username_error_too_long
    UsernameError.InvalidCharacters -> Res.string.auth_username_error_invalid_characters
}

private fun PasswordError.message(): StringResource = when (this) {
    PasswordError.Empty -> Res.string.auth_password_error_empty
    PasswordError.TooShort -> Res.string.auth_password_error_too_short
    PasswordError.TooLong -> Res.string.auth_password_error_too_long
}
