package com.bksd.profile.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.content_desc_avatar
import com.bksd.profile.presentation.content_desc_edit_avatar
import com.bksd.profile.presentation.member_since_prefix
import org.jetbrains.compose.resources.stringResource

private val MemberBadgeBackground = Color(0xFF1C1F2E)
private val PremiumBlue = Color(0xFF4A90D9)
private val DisabledGray = Color(0xFF6B7280)

@Composable
fun ProfileHeader(
    name: String,
    jobTitle: String,
    joinYear: String,
    isPremium: Boolean,
    avatarUrl: String? = null,
    isAvatarLoading: Boolean = false,
    onEditAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = Pair(isAvatarLoading, avatarUrl),
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                    },
                    label = "AvatarTransition"
                ) { (loading, url) ->
                    if (loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 2.5.dp
                        )
                    } else if (url != null) {
                        AsyncImage(
                            model = url,
                            contentDescription = stringResource(Res.string.content_desc_avatar),
                            modifier = Modifier.size(80.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(Res.string.content_desc_avatar),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(26.dp)
                    .offset(x = 2.dp, y = 2.dp)
                    .border(1.dp, MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onEditAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(Res.string.content_desc_edit_avatar),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = jobTitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        if (joinYear.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .background(
                        color = MemberBadgeBackground,
                        shape = RoundedCornerShape(percent = 50)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = if (isPremium) PremiumBlue else DisabledGray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(Res.string.member_since_prefix, joinYear),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileHeaderDarkPreview() {
    AppTheme(darkTheme = true) {
        ProfileHeader(
            name = "Alex Morgan",
            jobTitle = "Product Manager",
            joinYear = "2023",
            isPremium = true,
            onEditAvatarClick = {}
        )
    }
}

@Preview
@Composable
private fun ProfileHeaderLightPreview() {
    AppTheme(darkTheme = false) {
        ProfileHeader(
            name = "Alex Morgan",
            jobTitle = "Product Manager",
            joinYear = "2023",
            isPremium = false,
            isAvatarLoading = true,
            onEditAvatarClick = {},
        )
    }
}
