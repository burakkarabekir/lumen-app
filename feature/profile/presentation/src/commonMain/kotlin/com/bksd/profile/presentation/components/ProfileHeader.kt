package com.bksd.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.AppAvatar
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.member_since_prefix
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileHeader(
    name: String,
    jobTitle: String,
    joinYear: String,
    isPremium: Boolean,
    avatarUrl: String? = null,
    isAvatarLoading: Boolean = false,
    onAvatarClick: () -> Unit,
    onEditAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val memberBadgeBackground = Color(0xFF1C1F2E)
    val premiumBlue = Color(0xFF4A90D9)
    val disabledGray = Color(0xFF6B7280)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppAvatar(
            photoUrl = avatarUrl,
            size = 80.dp,
            isLoading = isAvatarLoading,
            contentDescription = "Profile avatar",
            showBorder = true,
            onClick = onAvatarClick,
            onEditClick = onEditAvatarClick
        )

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
                        color = memberBadgeBackground,
                        shape = RoundedCornerShape(percent = 50)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = if (isPremium) premiumBlue else disabledGray,
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
            onAvatarClick = {},
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
            onAvatarClick = {},
            onEditAvatarClick = {},
        )
    }
}
