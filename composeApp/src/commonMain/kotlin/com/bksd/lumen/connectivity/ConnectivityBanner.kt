package com.bksd.lumen.connectivity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileAccentGreen
import com.bksd.lumen.Res
import com.bksd.lumen.connection_restored
import com.bksd.lumen.no_connection
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

private const val RESTORED_VISIBLE_MS = 1_800L

@Composable
fun ConnectivityBanner(
    isOnline: Boolean,
    modifier: Modifier = Modifier,
) {
    var visible by remember { mutableStateOf(false) }
    var restored by remember { mutableStateOf(false) }

    LaunchedEffect(isOnline) {
        if (!isOnline) {
            restored = false
            visible = true
        } else if (visible) {
            restored = true
            delay(RESTORED_VISIBLE_MS.milliseconds)
            visible = false
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(tween(280)) { -it } + fadeIn(tween(280)),
        exit = slideOutVertically(tween(280)) { -it } + fadeOut(tween(280)),
        modifier = modifier,
    ) {
        val background by animateColorAsState(
            targetValue = if (restored) {
                MaterialTheme.colorScheme.extended.profileAccentGreen
            } else {
                MaterialTheme.colorScheme.error
            },
            animationSpec = tween(350),
            label = "ConnectivityBannerColor",
        )
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(
                    horizontal = MaterialTheme.dimens.spacing.md,
                    vertical = MaterialTheme.dimens.spacing.xs
                )
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
                .background(background)
                .padding(
                    horizontal = MaterialTheme.dimens.spacing.lg,
                    vertical = MaterialTheme.dimens.spacing.md,
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (restored) Icons.Default.CloudDone else Icons.Default.CloudOff,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.sm),
            )
            Spacer(Modifier.width(MaterialTheme.dimens.spacing.sm))
            Text(
                text = stringResource(
                    if (restored) Res.string.connection_restored else Res.string.no_connection,
                ),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview
@Composable
private fun ConnectivityBannerPreview() {
    AppTheme {
        ConnectivityBanner(isOnline = false)
    }
}
