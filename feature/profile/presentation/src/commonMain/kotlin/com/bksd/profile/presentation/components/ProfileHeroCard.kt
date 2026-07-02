package com.bksd.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.AppAvatar
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileHeroGradient
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.action_edit
import com.bksd.profile.presentation.profile_hero_avatar_content_desc
import com.bksd.profile.presentation.profile_hero_stat_entries
import com.bksd.profile.presentation.profile_hero_stat_joined
import com.bksd.profile.presentation.profile_hero_stat_weekly_streak
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileHeroCard(
    name: String,
    subtitle: String,
    avatarUrl: String?,
    isAvatarLoading: Boolean,
    entries: Int,
    weeklyStreak: Int,
    joinYear: String,
    onAvatarClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xxl))
            .background(Brush.linearGradient(MaterialTheme.colorScheme.extended.profileHeroGradient))
            .padding(MaterialTheme.dimens.spacing.xl)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppAvatar(
                photoUrl = avatarUrl,
                size = MaterialTheme.dimens.size.topBar,
                isLoading = isAvatarLoading,
                contentDescription = stringResource(Res.string.profile_hero_avatar_content_desc),
                showBorder = true,
                onClick = onAvatarClick
            )
            Spacer(Modifier.width(MaterialTheme.dimens.spacing.lg))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1
                )
                if (subtitle.isNotBlank()) {
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxs))
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1
                    )
                }
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable(onClick = onEditClick)
                    .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.sm)
            ) {
                Text(
                    text = stringResource(Res.string.action_edit),
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val stats = listOf(
                entries.toString() to stringResource(Res.string.profile_hero_stat_entries),
                weeklyStreak.toString() to stringResource(Res.string.profile_hero_stat_weekly_streak),
                joinYear to stringResource(Res.string.profile_hero_stat_joined)
            )
            stats.forEachIndexed { index, (value, label) ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = value,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxs))
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                if (index < stats.lastIndex) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(MaterialTheme.dimens.icon.xl)
                            .background(Color.White.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileHeroCardPreview() {
    AppTheme(darkTheme = true) {
        Box(Modifier.padding(MaterialTheme.dimens.spacing.lg)) {
            ProfileHeroCard(
                name = "Burak Yılmaz",
                subtitle = "Product Manager",
                avatarUrl = null,
                isAvatarLoading = false,
                entries = 83,
                weeklyStreak = 15,
                joinYear = "2024",
                onAvatarClick = {},
                onEditClick = {}
            )
        }
    }
}
