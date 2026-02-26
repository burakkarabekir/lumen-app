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
            text = "Create Your Space",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(LumenSpacing.sm))

        Text(
            text = "Start your journey with Momentum.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(LumenSpacing.xxxl))

        OutlinedTextField(
            value = state.fullName,
            onValueChange = { onAction(SignUpAction.OnFullNameChange(it)) },
            label = { Text("Full Name") },
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
            label = { Text("Email") },
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
            label = { Text("Password") },
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
                    text = "Must be at least 8 characters",
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
                    text = "Sign Up",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(LumenSpacing.lg))

        Text(
            text = "By signing up, you agree to our Terms and Privacy Policy.",
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
                text = "Already have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = " Sign In",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = LumenBrand500,
                modifier = Modifier.clickable { onAction(SignUpAction.OnSignInClick) }
            )
        }
    }
}
