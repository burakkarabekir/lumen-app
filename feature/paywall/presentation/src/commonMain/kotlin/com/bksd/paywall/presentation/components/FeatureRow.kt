package com.bksd.paywall.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.presentation.util.UiText
import com.bksd.paywall.presentation.PaywallFeatureUi

@Composable
fun FeatureRow(
    feature: PaywallFeatureUi,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.size.cancelIcon)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
            )
        }
        Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.lg))
        Column {
            Text(
                text = feature.title.asString(),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxs))
            Text(
                text = feature.description.asString(),
                style = MaterialTheme.typography.bodySmall.copy(
                    lineHeight = 18.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview
@Composable
private fun FeatureRowPreview() {
    PreviewAppTheme(darkTheme = true) {
        FeatureRow(
            feature = PaywallFeatureUi(
                title = UiText.Dynamic("AI Weekly Reflections"),
                description = UiText.Dynamic(
                    "Personal, written insights on your themes from the past week, refreshed as you write."
                ),
                icon = Icons.Default.AutoAwesome
            )
        )
    }
}
