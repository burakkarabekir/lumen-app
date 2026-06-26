@file:OptIn(ExperimentalMaterial3Api::class)

package com.bksd.journal.presentation.journal.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.labelXSmall
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.Mood
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.presentation.AudioPlaybackMode
import com.bksd.core.presentation.util.onSafe
import com.bksd.journal.presentation.util.DefaultMomentFormatter
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.TimeZone
import kotlin.time.Clock

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MomentCard(
    moment: Moment,
    formatter: MomentFormatter,
    timeZone: TimeZone,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    audioPlaybackState: PlaybackState = PlaybackState.STOPPED,
    audioCurrentPosition: String = "0:00",
    onAudioPlayClick: () -> Unit = {},
    onAudioPauseClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onFavoriteToggleClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val formattedDate = remember(moment.createdAt) {
        formatCardDate(moment.createdAt, timeZone)
    }

    val hasMoods = moment.moods.isNotEmpty()
    var moodPanelExpanded by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = moodPanelExpanded, label = "MoodReveal")

    val extendedColors = MaterialTheme.colorScheme.extended
    val moodColorList = remember(moment.moods, extendedColors) {
        moment.moods.map { moodColors(it, extendedColors).second.copy(alpha = 0.5f) }
    }
    val primaryMoodColor = moodColorList.firstOrNull() ?: Color.Transparent

    val panelWidth by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "PanelWidth"
    ) { expanded -> if (expanded) MomentCardDefaults.PANEL_EXPANDED_WIDTH_DP.dp else 0.dp }

    val contentBlur by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "ContentBlur"
    ) { expanded -> if (expanded) 6.dp else 0.dp }

    val gradientAlpha by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "GradientAlpha"
    ) { expanded -> if (expanded) 0.15f else 0f }

    val revealProgress by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "RevealProgress"
    ) { expanded -> if (expanded) 1f else 0f }

    var isActionsSheetVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .pointerInput(hasMoods) {
                if (!hasMoods) return@pointerInput
                val thresholdPx = MomentCardDefaults.SWIPE_THRESHOLD_DP.dp.toPx()
                var totalDrag = 0f
                var handled = false
                detectHorizontalDragGestures(
                    onDragStart = {
                        totalDrag = 0f
                        handled = false
                    },
                    onDragEnd = {
                        totalDrag = 0f
                        handled = false
                    }
                ) { _, dragAmount ->
                    totalDrag += dragAmount
                    if (!handled) {
                        if (totalDrag > thresholdPx) {
                            moodPanelExpanded = true
                            handled = true
                        } else if (totalDrag < -thresholdPx) {
                            moodPanelExpanded = false
                            handled = true
                        }
                    }
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .clip(RoundedCornerShape(14.dp))
        ) {
            if (hasMoods) {
                Spacer(modifier = Modifier.width(MomentCardDefaults.STRIP_WIDTH_DP.dp))
            }

            if (hasMoods && panelWidth > 0.dp) {
                MoodRevealPanel(
                    moods = moment.moods,
                    panelWidth = panelWidth,
                    revealProgress = revealProgress
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .then(if (contentBlur > 0.dp) Modifier.blur(contentBlur) else Modifier)
                    .padding(start = 2.dp)
                    .clickable { onClick() }
            ) {
                MediaContent(
                    moment = moment,
                    formatter = formatter,
                    audioPlaybackState = audioPlaybackState,
                    audioCurrentPosition = audioCurrentPosition,
                    onAudioPlayClick = onAudioPlayClick,
                    onAudioPauseClick = onAudioPauseClick
                )

                moment.location?.let { location ->
                    LocationBar(location = location)
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = moment.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    moment.body.onSafe {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    moment.tags.onSafe { items ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items.forEach { tag ->
                                Text(
                                    text = "#$tag",
                                    style = MaterialTheme.typography.labelXSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                AppDivider()
                CardFooter(
                    formattedDate = formattedDate,
                    isFavorite = moment.isFavorite,
                    onMoreClick = { isActionsSheetVisible = true }
                )
            }
        }

        if (gradientAlpha > 0f) {
            val density = LocalDensity.current
            val gradientEndX = with(density) {
                (panelWidth + MomentCardDefaults.STRIP_WIDTH_DP.dp + 40.dp).toPx()
            }
            val gradientColor = primaryMoodColor.copy(alpha = gradientAlpha)

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { clip = true }
                    .drawWithCache {
                        val brush = Brush.horizontalGradient(
                            colors = listOf(gradientColor, Color.Transparent),
                            endX = gradientEndX
                        )
                        onDrawBehind {
                            drawRect(brush)
                        }
                    }
            )
        }

        if (hasMoods) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(2.dp)
            ) {
                MoodColorStrip(
                    colors = moodColorList.toPersistentList(),
                    onClick = { moodPanelExpanded = !moodPanelExpanded }
                )
            }
        }
    }

    if (isActionsSheetVisible) {
        MomentActionsSheet(
            onDismiss = { isActionsSheetVisible = false },
            onEditClick = onEditClick,
            onFavoriteClick = onFavoriteToggleClick,
            onDeleteClick = onDeleteClick,
            isLiked = moment.isFavorite
        )
    }
}

@Composable
private fun MoodColorStrip(
    colors: ImmutableList<Color>,
    onClick: () -> Unit
) {
    val stripBrush = remember(colors) {
        if (colors.isEmpty()) return@remember Brush.verticalGradient(listOf(Color.Gray, Color.Gray))
        if (colors.size == 1) return@remember Brush.verticalGradient(listOf(colors[0], colors[0]))

        val stops = mutableListOf<Pair<Float, Color>>()
        val segmentSize = 1f / colors.size
        colors.forEachIndexed { index, color ->
            val start = index * segmentSize
            val end = (index + 1) * segmentSize
            stops.add(start to color)
            stops.add(end to color)
        }
        Brush.verticalGradient(colorStops = stops.toTypedArray())
    }

    Box(
        modifier = Modifier
            .width(MomentCardDefaults.STRIP_WIDTH_DP.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
            .background(stripBrush)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun MoodRevealPanel(
    moods: List<Mood>,
    panelWidth: Dp,
    revealProgress: Float
) {
    val extendedColors = MaterialTheme.colorScheme.extended

    Column(
        modifier = Modifier
            .width(panelWidth)
            .clipToBounds()
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val height = (placeable.height * revealProgress).roundToInt().coerceAtLeast(0)
                layout(placeable.width, height) {
                    placeable.place(0, 0)
                }
            }
            .padding(vertical = 10.dp, horizontal = 6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        moods.forEachIndexed { index, mood ->
            val staggerDelay = index * 0.12f
            val chipAlpha = ((revealProgress - staggerDelay) / 0.35f).coerceIn(0f, 1f)

            val (bg, textColor) = moodColors(mood, extendedColors)
            Row(
                modifier = Modifier
                    .graphicsLayer { alpha = chipAlpha }
                    .clip(RoundedCornerShape(8.dp))
                    .background(bg)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = mood.emoji, fontSize = 12.sp)
                Text(
                    text = mood.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun MediaContent(
    moment: Moment,
    formatter: MomentFormatter,
    audioPlaybackState: PlaybackState,
    audioCurrentPosition: String,
    onAudioPlayClick: () -> Unit,
    onAudioPauseClick: () -> Unit
) {
    val attachments = moment.attachments
    if (attachments.isEmpty()) return

    when (val primary = attachments.first()) {
        is PhotoAttachment -> {
            PhotoPreview(url = primary.remoteUrl.value)
        }

        is VideoAttachment -> {
            VideoPreview(durationMs = primary.durationMs, formatter = formatter)
        }

        is AudioAttachment -> {
            Box(modifier = Modifier.padding(12.dp)) {
                AudioPreview(
                    playbackState = audioPlaybackState,
                    currentPositionFormatted = audioCurrentPosition,
                    mode = AudioPlaybackMode.STANDARD,
                    durationFormatted = if (primary.durationMs > 0)
                        formatter.formatDuration(primary.durationMs) else "0:00",
                    onPlayClick = onAudioPlayClick,
                    onPauseClick = onAudioPauseClick
                )
            }
        }

        is LinkAttachment -> {
            Box(modifier = Modifier.padding(12.dp)) {
                LinkPreview(url = primary.url.value)
            }
        }
    }
}

@Composable
private fun CardFooter(
    formattedDate: String,
    isFavorite: Boolean,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorited",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
            }
            IconButton(
                onClick = onMoreClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More actions",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMomentCard() {
    AppTheme {
        MomentCard(
            moment = Moment(
                id = "0",
                title = "A quiet morning walk",
                body = "The air was crisp and the sun had just begun to rise.",
                createdAt = Clock.System.now(),
                moods = listOf(Mood.CALM, Mood.REFLECTIVE)
            ),
            formatter = DefaultMomentFormatter(TimeZone.UTC),
            onClick = {},
            timeZone = TimeZone.UTC
        )
    }
}

@Preview
@Composable
private fun PreviewMomentCardDark() {
    AppTheme(darkTheme = true) {
        MomentCard(
            moment = Moment(
                id = "0",
                title = "Creative burst at midnight",
                body = "Ideas flowing like a river. Wrote three pages without stopping.",
                tags = listOf("writing", "flow"),
                createdAt = Clock.System.now(),
                moods = listOf(Mood.INSPIRED, Mood.ENERGETIC, Mood.CREATIVE)
            ),
            formatter = DefaultMomentFormatter(TimeZone.UTC),
            onClick = {},
            timeZone = TimeZone.UTC
        )
    }
}
