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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.destructiveSecondaryOutline
import com.bksd.core.design_system.theme.disabledFill
import com.bksd.core.design_system.theme.disabledOutline


@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AppButtonStyle = AppButtonStyle.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    cornerRadius: Dp = 12.dp,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val colors = when (style) {
        AppButtonStyle.PRIMARY -> {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }

        AppButtonStyle.DESTRUCTIVE_PRIMARY -> {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContainerColor = MaterialTheme.colorScheme.disabledFill,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }

        AppButtonStyle.SECONDARY -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }

        AppButtonStyle.DESTRUCTIVE_SECONDARY -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.error,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }

        AppButtonStyle.TEXT -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.error,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }
    }

    val defaultBorderStroke = BorderStroke(
        width = .5.dp,
        color = MaterialTheme.colorScheme.disabledOutline
    )

    val border = when (style) {
        AppButtonStyle.PRIMARY,
        AppButtonStyle.DESTRUCTIVE_PRIMARY,
            -> defaultBorderStroke.takeIf { !enabled }

        AppButtonStyle.SECONDARY -> BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
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
        shape = RoundedCornerShape(cornerRadius),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(MaterialTheme.dimens.spacing.sm)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.sm)
                    .alpha(
                        alpha = if (isLoading) 1f else 0f
                    ),
                strokeWidth = 1.5.dp,
                color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.spacing.sm,
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
                    style = MaterialTheme.typography.titleSmall,
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
fun AppPrimaryButtonDisabledPreview() {
    AppTheme(
        darkTheme = true
    ) {
        AppButton(
            text = "Hello world!",
            onClick = {},
            style = AppButtonStyle.PRIMARY,
            enabled = false
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