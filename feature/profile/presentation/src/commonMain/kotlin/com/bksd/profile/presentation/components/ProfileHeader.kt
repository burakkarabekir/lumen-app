package com.bksd.profile.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun ProfileHeader(
    displayName: String,
    role: String,
    memberSince: String,
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
        // Avatar circle
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    // The trick to creating the cutout effect is defining a border matching the background
                    .border(2.dp, MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = Pair(isAvatarLoading, avatarUrl),
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                            animationSpec = tween(
                                300
                            )
                        )
                    },
                    label = "AvatarTransition"
                ) { (loading, url) ->
                    if (loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp),
                            strokeWidth = 3.dp
                        )
                    } else if (url != null) {
                        AsyncImage(
                            model = url,
                            contentDescription = stringResource(Res.string.content_desc_avatar),
                            modifier = Modifier.size(100.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(Res.string.content_desc_avatar),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
            // Edit badge
            Box(
                modifier = Modifier
                    .size(28.dp)
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
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display name
        Text(
            text = displayName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(2.dp))

        // Role
        Text(
            text = role,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Member since badge
        Text(
            text = stringResource(Res.string.member_since_prefix, memberSince),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun ProfileHeaderDarkPreview() {
    AppTheme(darkTheme = true) {
        ProfileHeader(
            displayName = "Alex Morgan",
            role = "Product Manager",
            memberSince = "Member since 2023",
            onEditAvatarClick = {}
        )
    }
}

@Preview
@Composable
private fun ProfileHeaderLightPreview() {
    AppTheme(darkTheme = false) {
        ProfileHeader(
            displayName = "Alex Morgan",
            role = "Product Manager",
            memberSince = "Member since 2023",
            onEditAvatarClick = {}
        )
    }
}
