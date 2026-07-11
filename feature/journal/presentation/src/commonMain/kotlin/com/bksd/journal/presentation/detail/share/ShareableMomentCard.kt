package com.bksd.journal.presentation.detail.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.ShareStyle
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.moodHue
import com.bksd.core.design_system.theme.shareCardColors
import com.bksd.core.domain.model.Mood
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.journal.presentation.model.MomentUi
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Clock

@Composable
fun ShareableMomentCard(
    moment: MomentUi,
    style: ShareStyle,
    format: ShareFormat,
    showDate: Boolean,
    showLocation: Boolean,
    showMood: Boolean,
    dateLabel: String,
    modifier: Modifier = Modifier,
) {
    val colors = shareCardColors(style)
    val photoUrl = moment.attachments.filterIsInstance<PhotoAttachment>().firstOrNull()?.remoteUrl?.value
    val quote = moment.body?.takeIf { it.isNotBlank() } ?: moment.title
    val firstMood = moment.moods.firstOrNull()

    Box(
        modifier = modifier
            .aspectRatio(format.ratio)
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xxl))
            .background(
                if (colors.gradient) {
                    Brush.linearGradient(listOf(colors.backgroundTop, colors.backgroundBottom))
                } else {
                    Brush.linearGradient(listOf(colors.backgroundTop, colors.backgroundTop))
                }
            )
    ) {
        if (style == ShareStyle.PHOTO && photoUrl != null) {
            SubcomposeAsyncImage(
                model = photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = {
                    Box(
                        Modifier.fillMaxSize().background(
                            Brush.linearGradient(listOf(colors.backgroundTop, colors.backgroundBottom))
                        )
                    )
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.25f), Color.Black.copy(alpha = 0.72f))
                        )
                    )
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(MaterialTheme.dimens.spacing.xxxl)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.tile).clip(RoundedCornerShape(MaterialTheme.dimens.radius.md)).background(colors.logoBg)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = null,
                        tint = colors.logoTint,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.xl)
                    )
                }
                Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
                Text(
                    text = "Lumen",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.text
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxxl))

            Text(
                text = "“",
                fontSize = 52.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Black,
                color = colors.text.copy(alpha = 0.85f)
            )
            Text(
                text = quote,
                fontSize = 29.sp,
                lineHeight = 37.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.4).sp,
                color = colors.text,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xs)
            )

            Spacer(Modifier.weight(1f))

            if (showMood && firstMood != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                    modifier = Modifier
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.full))
                        .background(colors.chipBg)
                        .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.sm)
                ) {
                    Box(Modifier.size(MaterialTheme.dimens.icon.xs).clip(CircleShape).background(MaterialTheme.colorScheme.extended.moodHue(firstMood)))
                    Text(
                        text = firstMood.label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.text
                    )
                }
                Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
            }

            Box(Modifier.fillMaxWidth().height(1.dp).background(colors.divider))
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    if (showDate) {
                        Text(
                            text = dateLabel,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.text
                        )
                    }
                    val location = moment.location?.displayName
                    if (showLocation && !location.isNullOrBlank()) {
                        Text(
                            text = location,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.sub,
                            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xxs)
                        )
                    }
                }
                Text(
                    text = "lumen.app",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.sub
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShareableMomentCardPreview() {
    PreviewAppTheme {
        ShareableMomentCard(
            moment = MomentUi(
                id = "1",
                title = "Morning pages",
                body = "I want more mornings that start this slowly.",
                createdAt = Clock.System.now(),
                moods = persistentListOf(Mood.CALM),
                tags = persistentListOf(),
                attachments = persistentListOf(),
                location = null,
                isFavorite = false,
            ),
            style = ShareStyle.AURORA,
            format = ShareFormat.PORTRAIT,
            showDate = true,
            showLocation = true,
            showMood = true,
            dateLabel = "Saturday, June 27",
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg).width(320.dp)
        )
    }
}
