package com.bksd.auth.presentation.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.auth.presentation.Res
import com.bksd.auth.presentation.btn_sign_in
import com.bksd.auth.presentation.continue_with_google
import com.bksd.auth.presentation.forgot_password
import com.bksd.auth.presentation.label_email
import com.bksd.auth.presentation.label_password
import com.bksd.auth.presentation.no_account_prompt
import com.bksd.auth.presentation.remember_me
import com.bksd.auth.presentation.sign_in_subtitle
import com.bksd.auth.presentation.sign_up_link
import com.bksd.auth.presentation.signin.components.SocialLoginDivider
import com.bksd.auth.presentation.welcome_back
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.LumenBase600
import com.bksd.core.design_system.theme.LumenBrand500
import com.bksd.core.design_system.theme.LumenRadius
import com.bksd.core.design_system.theme.LumenSpacing
import com.bksd.core.presentation.auth.GoogleSignInResult
import com.bksd.core.presentation.auth.rememberGoogleSignInLauncher
import com.bksd.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInRoot(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: SignInViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SignInEvent.SignInSuccess -> onNavigateToHome()
            is SignInEvent.NavigateToSignUp -> onNavigateToSignUp()
            is SignInEvent.NavigateToForgotPassword -> onNavigateToForgotPassword()
            is SignInEvent.SignInError -> {
            }
        }
    }

    SignInScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
internal fun SignInScreen(
    state: SignInState,
    onAction: (SignInAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val googleLauncher = rememberGoogleSignInLauncher { result ->
        when (result) {
            is GoogleSignInResult.Success ->
                onAction(SignInAction.OnGoogleIdTokenReceived(result.idToken))
            GoogleSignInResult.Cancelled ->
                onAction(SignInAction.OnGoogleSignInFailed(cancelled = true))
            is GoogleSignInResult.Failed ->
                onAction(SignInAction.OnGoogleSignInFailed(cancelled = false))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(LumenSpacing.xxl),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(Res.string.welcome_back),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(LumenSpacing.sm))

        Text(
            text = stringResource(Res.string.sign_in_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(LumenSpacing.xxxl))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onAction(SignInAction.OnEmailChange(it)) },
            label = { Text(stringResource(Res.string.label_email)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(LumenRadius.md),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LumenBrand500,
                unfocusedBorderColor = LumenBase600
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(LumenSpacing.md))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onAction(SignInAction.OnPasswordChange(it)) },
            label = { Text(stringResource(Res.string.label_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(LumenRadius.md),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LumenBrand500,
                unfocusedBorderColor = LumenBase600
            ),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = state.rememberMe,
                    onCheckedChange = { onAction(SignInAction.OnRememberMeToggle(it)) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = LumenBrand500,
                        uncheckedColor = LumenBase600,
                    ),
                )
                Text(
                    text = stringResource(Res.string.remember_me),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )
            }
            TextButton(
                onClick = { onAction(SignInAction.OnForgotPasswordClick) }
            ) {
                Text(
                    text = stringResource(Res.string.forgot_password),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LumenBrand500
                )
            }
        }

        Spacer(modifier = Modifier.height(LumenSpacing.xl))

        if (state.error != null) {
            Text(
                text = state.error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = LumenSpacing.md)
            )
        }

        AppButton(
            text = stringResource(Res.string.btn_sign_in),
            onClick = { onAction(SignInAction.OnSignInClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = state.isSubmitEnabled,
            isLoading = state.isLoading,
            style = AppButtonStyle.PRIMARY
        )

        Spacer(modifier = Modifier.height(LumenSpacing.lg))

        SocialLoginDivider()

        Spacer(modifier = Modifier.height(LumenSpacing.lg))

        AppButton(
            text = stringResource(Res.string.continue_with_google),
            onClick = { googleLauncher.launch() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !state.isLoading && !state.isSocialLoading,
            isLoading = state.isSocialLoading,
            style = AppButtonStyle.SECONDARY
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = LumenSpacing.xl)
        ) {
            Text(
                text = stringResource(Res.string.no_account_prompt),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = stringResource(Res.string.sign_up_link),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = LumenBrand500,
                modifier = Modifier.clickable { onAction(SignInAction.OnSignUpClick) }
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    AppTheme {
        SignInScreen(
            state = SignInState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDark() {
    AppTheme(darkTheme = true) {
        SignInScreen(
            state = SignInState(),
            onAction = {}
        )
    }
}