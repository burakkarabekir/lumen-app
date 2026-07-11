package com.bksd.paywall.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.aiIconGradient
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.paywall.presentation.Res
import com.bksd.paywall.presentation.paywall_subtitle
import com.bksd.paywall.presentation.paywall_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HeroCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .background(
                Brush.verticalGradient(colors = MaterialTheme.colorScheme.extended.paywallHeroGradient)
            )
            .padding(vertical = MaterialTheme.dimens.spacing.huge),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(MaterialTheme.dimens.size.topBar)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xxl))
                    .background(
                        Brush.linearGradient(colors = MaterialTheme.colorScheme.extended.aiIconGradient)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.avatar)
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xl))

            Text(
                text = stringResource(Res.string.paywall_title),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

            Text(
                text = stringResource(Res.string.paywall_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp
                ),
                color = Color.White.copy(alpha = 0.55f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun HeroCardLightPreview() {
    PreviewAppTheme(darkTheme = false) {
        HeroCard()
    }
}

@Preview
@Composable
private fun HeroCardDarkPreview() {
    PreviewAppTheme(darkTheme = true) {
        HeroCard()
    }
}
