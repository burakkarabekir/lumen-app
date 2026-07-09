package com.bksd.lumen.lock

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.coverGradient
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.lumen.Res
import com.bksd.lumen.lock_gate_subtitle
import com.bksd.lumen.lock_gate_title
import com.bksd.lumen.lock_gate_unlock
import org.jetbrains.compose.resources.stringResource

@Composable
fun LockGateScreen(
    onUnlock: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) { detectTapGestures { } }
            .padding(MaterialTheme.dimens.spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.size.topBar)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xxl))
                .background(Brush.linearGradient(MaterialTheme.colorScheme.extended.coverGradient)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.avatar),
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xl))

        Text(
            text = stringResource(Res.string.lock_gate_title),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

        Text(
            text = stringResource(Res.string.lock_gate_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxxl))

        AppButton(
            text = stringResource(Res.string.lock_gate_unlock),
            onClick = onUnlock,
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.size.fab),
            style = AppButtonStyle.PRIMARY,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Fingerprint,
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.md),
                )
            },
        )
    }
}

@Preview
@Composable
private fun LockGateScreenPreview() {
    AppTheme(darkTheme = true) {
        LockGateScreen(onUnlock = {})
    }
}
