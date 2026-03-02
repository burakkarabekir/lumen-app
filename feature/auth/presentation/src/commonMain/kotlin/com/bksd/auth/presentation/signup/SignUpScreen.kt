package com.bksd.auth.presentation.signup

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.LumenBase600
import com.bksd.core.design_system.theme.LumenBrand500
import com.bksd.core.design_system.theme.LumenBrand600
import com.bksd.core.design_system.theme.LumenRadius
import com.bksd.core.design_system.theme.LumenSpacing
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.auth.presentation.Res
import com.bksd.auth.presentation.already_have_account
import com.bksd.auth.presentation.btn_sign_up
import com.bksd.auth.presentation.create_your_space
import com.bksd.auth.presentation.label_email
import com.bksd.auth.presentation.label_full_name
import com.bksd.auth.presentation.label_password
import com.bksd.auth.presentation.password_hint
import com.bksd.auth.presentation.sign_in_link
import com.bksd.auth.presentation.sign_up_subtitle
import com.bksd.auth.presentation.terms_agreement
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpRoot(
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: SignUpViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SignUpEvent.SignUpSuccess -> onNavigateToHome()
            is SignUpEvent.NavigateToSignIn -> onNavigateToSignIn()
            is SignUpEvent.SignUpError -> {
                // Handle error
            }

            is SignUpEvent.OpenPrivacyPolicy -> {
                // Open web URL
            }

            is SignUpEvent.OpenTermsOfService -> {
                // Open web URL
            }
        }
    }

    SignUpScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
internal fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier
) {
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
            text = stringResource(Res.string.create_your_space),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(LumenSpacing.sm))

        Text(
            text = stringResource(Res.string.sign_up_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(LumenSpacing.xxxl))

        OutlinedTextField(
            value = state.fullName,
            onValueChange = { onAction(SignUpAction.OnFullNameChange(it)) },
            label = { Text(stringResource(Res.string.label_full_name)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
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
            value = state.email,
            onValueChange = { onAction(SignUpAction.OnEmailChange(it)) },
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
            onValueChange = { onAction(SignUpAction.OnPasswordChange(it)) },
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
            supportingText = {
                Text(
                    text = stringResource(Res.string.password_hint),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(LumenSpacing.xl))

        if (state.error != null) {
            Text(
                text = state.error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = LumenSpacing.md)
            )
        }

        Button(
            onClick = { onAction(SignUpAction.OnSignUpClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = state.isSubmitEnabled,
            shape = RoundedCornerShape(LumenRadius.full),
            colors = ButtonDefaults.buttonColors(
                containerColor = LumenBrand600,
                disabledContainerColor = LumenBrand600.copy(alpha = 0.5f)
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(LumenSpacing.sm)
                )
            } else {
                Text(
                    text = stringResource(Res.string.btn_sign_up),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(LumenSpacing.lg))

        Text(
            text = stringResource(Res.string.terms_agreement),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = LumenSpacing.xl)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = LumenSpacing.xl)
        ) {
            Text(
                text = stringResource(Res.string.already_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = stringResource(Res.string.sign_in_link),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = LumenBrand500,
                modifier = Modifier.clickable { onAction(SignUpAction.OnSignInClick) }
            )
        }
    }
}
