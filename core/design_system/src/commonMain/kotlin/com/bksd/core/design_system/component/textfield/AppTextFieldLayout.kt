 package com.bksd.core.design_system.component.textfield

 import androidx.compose.foundation.background
 import androidx.compose.foundation.border
 import androidx.compose.foundation.clickable
 import androidx.compose.foundation.interaction.MutableInteractionSource
 import androidx.compose.foundation.interaction.collectIsFocusedAsState
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.shape.RoundedCornerShape
 import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.Text
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.LaunchedEffect
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.remember
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.tooling.preview.Preview
 import androidx.compose.ui.unit.dp
 import com.bksd.core.design_system.theme.AppTheme
 import com.bksd.core.design_system.theme.textFieldBgPassive
 import com.bksd.core.design_system.theme.textPrimary
 import com.bksd.core.design_system.theme.textTertiary

 @Composable
 fun AppTextFieldLayout(
    title: String? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    enabled: Boolean = true,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textField: @Composable (Modifier, MutableInteractionSource) -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onFocusChanged(isFocused)
    }

    val textFieldStyleModifier = Modifier
        .fillMaxWidth()
        .background(
            color = when {
                isFocused -> MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.05f
                )

                enabled -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.textFieldBgPassive
            },
            shape = RoundedCornerShape(8.dp)
        )
        .border(
            width = 1.dp,
            color = when {
                isError -> MaterialTheme.colorScheme.error
                isFocused -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline
            },
            shape = RoundedCornerShape(8.dp)
        )
        .padding(12.dp)

    Column(
        modifier = modifier
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.textPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        textField(textFieldStyleModifier, interactionSource)

        if (supportingText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = supportingText,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.textTertiary
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        AppTextFieldLayout(
            title = "Title",
            isError = true,
            supportingText = "Supporting text",
            onFocusChanged = {},
            textField = { modifier, interactionSource ->
                Text(
                    text = "Text field",
                    modifier = modifier
                        .clickable(
                            enabled = true,
                            interactionSource = interactionSource,
                            onClick = {}
                        ),
                )
            }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        AppTextFieldLayout(
            title = "Title",
            isError = true,
            supportingText = "Supporting text",
            onFocusChanged = {},
            textField = { modifier, interactionSource ->
                Text(
                    text = "Text field",
                    modifier = modifier
                        .clickable(
                            enabled = true,
                            interactionSource = interactionSource,
                            onClick = {}
                        ),
                )
            }
        )
    }
}