package com.bksd.auth.presentation.resetpassword

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
import androidx.compose.ui.text.input.KeyboardType
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
fun ResetPasswordRoot(
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ResetPasswordViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ResetPasswordEvent.NavigateToSignIn -> onNavigateToSignIn()
            is ResetPasswordEvent.ResetPasswordSuccess -> {
                // Handle success visual indication (can be handled by local state too)
            }

            is ResetPasswordEvent.ResetPasswordError -> {
                // Handle error
            }
        }
    }

    ResetPasswordScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
internal fun ResetPasswordScreen(
    state: ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
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
            text = "Reset Password",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(LumenSpacing.sm))

        Text(
            text = "Enter the email address associated with your Momentum account and we'll send you a secure link to reset your password.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LumenSpacing.xxxl))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onAction(ResetPasswordAction.OnEmailChange(it)) },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
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

        Spacer(modifier = Modifier.height(LumenSpacing.xl))

        if (state.error != null) {
            Text(
                text = state.error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = LumenSpacing.md)
            )
        }

        if (state.isSuccess) {
            Text(
                text = "Password reset email sent!",
                color = LumenBrand500,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = LumenSpacing.md)
            )
        }

        Button(
            onClick = { onAction(ResetPasswordAction.OnSubmitClick) },
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
                    text = "Send Link",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = LumenSpacing.xl)
        ) {
            Text(
                text = "Back to Sign In",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = LumenBrand500,
                modifier = Modifier.clickable { onAction(ResetPasswordAction.OnBackToSignInClick) }
            )
        }
    }
}
