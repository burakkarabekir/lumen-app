package com.bksd.auth.presentation.signin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.auth.presentation.Res
import com.bksd.auth.presentation.continue_with_apple
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import org.jetbrains.compose.resources.stringResource

private const val APPLE_LOGO_PATH =
    "M318.7 268.7c-.2-36.7 16.4-64.4 50-84.8-18.8-26.9-47.2-41.7-84.7-44.6-35.5-2.8-74.3 20.7-88.5 " +
        "20.7-15 0-49.4-19.7-76.4-19.7C63.3 141.2 4 184.8 4 273.5q0 39.3 14.4 81.2c12.8 36.7 59 126.7 " +
        "107.2 125.2 25.2-.6 43-17.9 75.8-17.9 31.8 0 48.3 17.9 76.4 17.9 48.6-.7 90.4-82.5 102.6-119.3-65.2" +
        "-30.7-61.7-90-61.7-91.9zm-56.6-164.2c27.3-32.4 24.8-61.9 24-72.5-24.1 1.4-52 16.4-67.9 34.9-17.5 " +
        "19.8-27.8 44.3-25.6 71.9 26.1 2 49.9-11.4 69.5-34.3z"

@Composable
fun ContinueWithAppleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val containerColor = if (dark) Color.White else Color.Black
    val contentColor = if (dark) Color.Black else Color.White

    val appleLogo = remember {
        ImageVector.Builder(
            name = "AppleLogo",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 384f,
            viewportHeight = 512f,
        ).apply {
            addPath(
                pathData = PathParser().parsePathString(APPLE_LOGO_PATH).toNodes(),
                fill = SolidColor(Color.Black),
            )
        }.build()
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.4f),
            disabledContentColor = contentColor.copy(alpha = 0.6f),
        ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.sm),
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.sm)
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = contentColor,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.spacing.sm,
                    Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
            ) {
                Icon(
                    imageVector = appleLogo,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.lg),
                )
                Text(
                    text = stringResource(Res.string.continue_with_apple),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ContinueWithAppleButtonPreview() {
    AppTheme {
        ContinueWithAppleButton(onClick = {})
    }
}

@Preview
@Composable
private fun ContinueWithAppleButtonDarkPreview() {
    AppTheme(darkTheme = true) {
        ContinueWithAppleButton(onClick = {})
    }
}
