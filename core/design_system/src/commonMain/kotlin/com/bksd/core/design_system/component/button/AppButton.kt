package com.bksd.core.design_system.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.destructiveSecondaryOutline
import com.bksd.core.design_system.theme.disabledFill
import com.bksd.core.design_system.theme.disabledOutline
import com.bksd.core.design_system.theme.textDisabled
import com.bksd.core.design_system.theme.textSecondary


enum class AppButtonStyle {
    PRIMARY,
    DESTRUCTIVE_PRIMARY,
    SECONDARY,
    DESTRUCTIVE_SECONDARY,
    TEXT
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AppButtonStyle = AppButtonStyle.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val colors = when (style) {
        AppButtonStyle.PRIMARY -> {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.disabledFill,
                disabledContentColor = MaterialTheme.colorScheme.textDisabled
            )
        }

        AppButtonStyle.DESTRUCTIVE_PRIMARY -> {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContainerColor = MaterialTheme.colorScheme.disabledFill,
                disabledContentColor = MaterialTheme.colorScheme.textDisabled
            )
        }

        AppButtonStyle.SECONDARY -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.textSecondary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.textDisabled
            )
        }

        AppButtonStyle.DESTRUCTIVE_SECONDARY -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.error,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.textDisabled
            )
        }

        AppButtonStyle.TEXT -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.textDisabled
            )
        }
    }

    val defaultBorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.disabledOutline
    )

    val border = when (style) {
        AppButtonStyle.PRIMARY,
        AppButtonStyle.DESTRUCTIVE_PRIMARY,
            -> defaultBorderStroke.takeIf { !enabled }

        AppButtonStyle.SECONDARY -> defaultBorderStroke
        AppButtonStyle.DESTRUCTIVE_SECONDARY -> {
            val borderColor = if (enabled) {
                MaterialTheme.colorScheme.destructiveSecondaryOutline
            } else {
                MaterialTheme.colorScheme.disabledOutline
            }
            BorderStroke(
                width = 1.dp,
                color = borderColor
            )
        }

        else -> null
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        enabled = enabled && !isLoading,
        border = border,
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(6.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .alpha(
                        alpha = if (isLoading) 1f else 0f
                    ),
                strokeWidth = 1.5.dp,
                color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(
                    if (isLoading) 0f else 1f
                )
            ) {
                leadingIcon?.invoke()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
@Preview
fun AppPrimaryButtonPreview() {
    AppTheme(
        darkTheme = true
    ) {
        AppButton(
            text = "Hello world!",
            onClick = {},
            style = AppButtonStyle.PRIMARY
        )
    }
}

@Composable
@Preview
fun AppSecondaryButtonPreview() {
    AppTheme(
        darkTheme = true
    ) {
        AppButton(
            text = "Hello world!",
            onClick = {},
            style = AppButtonStyle.SECONDARY
        )
    }
}

@Composable
@Preview
fun AppDestructivePrimaryButtonPreview() {
    AppTheme(
        darkTheme = true
    ) {
        AppButton(
            text = "Hello world!",
            onClick = {},
            style = AppButtonStyle.DESTRUCTIVE_PRIMARY
        )
    }
}

@Composable
@Preview
fun AppDestructiveSecondaryButtonPreview() {
    AppTheme(
        darkTheme = true
    ) {
        AppButton(
            text = "Hello world!",
            onClick = {},
            style = AppButtonStyle.DESTRUCTIVE_SECONDARY
        )
    }
}

@Composable
@Preview
fun AppTextButtonPreview() {
    AppTheme(
        darkTheme = true
    ) {
        AppButton(
            text = "Hello world!",
            onClick = {},
            style = AppButtonStyle.TEXT
        )
    }
}