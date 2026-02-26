package com.bksd.core.design_system.component.button.fab

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.primary95

@Composable
fun SpeakingBubbleFab(
    showBubble: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    colors: BubbleFabColor = rememberBubbleFabColor(),
    buttonSize: Dp = 56.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = modifier
            .background(
                brush = if (showBubble) {
                    colors.innerCircle
                } else SolidColor(Color.Transparent),
                shape = CircleShape
            )
            .padding(6.dp)
            .background(
                color = if (showBubble) {
                    MaterialTheme.colorScheme.primary95
                } else {
                    Color.Transparent
                },
                shape = CircleShape
            )
            .padding(4.dp)
            .background(
                brush = if (isPressed) {
                    colors.primaryPressed
                } else colors.primary,
                shape = CircleShape
            )
            .size(buttonSize)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }

}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        SpeakingBubbleFab(
            showBubble = true,
            onClick = {},
            icon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Echo Bubble Button",
                    tint = MaterialTheme.colorScheme.surface
                )
            }

        )
    }
}

@Preview
@Composable
private fun PreviewDoNotShowButton() {
    AppTheme {
        SpeakingBubbleFab(
            showBubble = false,
            onClick = {},
            icon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Echo Bubble Button",
                    tint = MaterialTheme.colorScheme.surface
                )
            }

        )
    }
}