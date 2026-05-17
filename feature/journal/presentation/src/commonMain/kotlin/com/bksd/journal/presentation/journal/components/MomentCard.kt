package com.bksd.journal.presentation.journal.components

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.labelXSmall
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.presentation.AudioPlaybackMode
import com.bksd.core.presentation.util.onSafe
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
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
    onDeleteClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
) {
    val formattedDate = remember(moment.createdAt) {
        formatCardDate(moment.createdAt, timeZone)
    }

    val hasMoods = moment.moods.isNotEmpty()
    var swipeState by remember { mutableStateOf(SwipeState.NONE) }
    val transition = updateTransition(targetState = swipeState, label = "SwipeReveal")

    // ── Mood colors for the strip (use vibrant text color, not pastel bg) ──
    val extendedColors = MaterialTheme.colorScheme.extended
    val moodColorList = remember(moment.moods, extendedColors) {
        moment.moods.map { moodColors(it, extendedColors).second.copy(alpha = 0.5f) }
    }
    val primaryMoodColor = moodColorList.firstOrNull() ?: Color.Transparent

    // ── Animated properties (unified ease-in-out curve) ──
    // All properties share the same tween spec for perfectly synchronized phasing:
    //   Phase 1 (0–30%):  Slow start — acknowledges the user's touch
    //   Phase 2 (30–70%): Rapid change — panel expands, tags fade in
    //   Phase 3 (70–100%): Smooth deceleration — blur & gradient settle

    val panelWidth by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "PanelWidth"
    ) { state -> if (state == SwipeState.MOODS) MomentCardDefaults.PANEL_EXPANDED_WIDTH_DP.dp else 0.dp }

    val actionPanelWidth by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "ActionPanelWidth"
    ) { state -> if (state == SwipeState.ACTIONS) MomentCardDefaults.ACTION_PANEL_WIDTH_DP.dp else 0.dp }

    val contentBlur by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "ContentBlur"
    ) { state -> if (state != SwipeState.NONE) 6.dp else 0.dp }

    val gradientAlpha by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "GradientAlpha"
    ) { state -> if (state == SwipeState.MOODS) 0.15f else 0f }

    val revealProgress by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "RevealProgress"
    ) { state -> if (state == SwipeState.MOODS) 1f else 0f }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = MomentCardDefaults.REVEAL_DURATION_MS,
                    easing = MomentCardDefaults.RevealEasing
                )
            )
            .then(
                Modifier.pointerInput(Unit) {
                    var startState = SwipeState.NONE
                    detectHorizontalDragGestures(
                        onDragStart = { startState = swipeState }
                    ) { _, dragAmount ->
                        if (dragAmount > MomentCardDefaults.DRAG_THRESHOLD) {
                            if (startState == SwipeState.ACTIONS) swipeState = SwipeState.NONE
                            else if (hasMoods) swipeState = SwipeState.MOODS
                        } else if (dragAmount < -MomentCardDefaults.DRAG_THRESHOLD) {
                            swipeState = if (startState == SwipeState.MOODS) SwipeState.NONE
                            else SwipeState.ACTIONS
                        }
                    }
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(2.dp)
                .clip(RoundedCornerShape(14.dp))
        ) {
            // ── 1. Mood Color Strip (pinned left, always visible) ──
            if (hasMoods) {
                MoodColorStrip(
                    colors = moodColorList.toPersistentList(),
                    onClick = {
                        swipeState =
                            if (swipeState == SwipeState.MOODS) SwipeState.NONE else SwipeState.MOODS
                    }
                )
            }

            // ── 2. Expandable Mood Panel ──
            if (hasMoods && panelWidth > 0.dp) {
                MoodRevealPanel(
                    moods = moment.moods,
                    panelWidth = panelWidth,
                    revealProgress = revealProgress
                )
            }

            // ── 3. Card Content (blurred when mood panel is expanded) ──
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
                    onMoreClick = onMoreClick
                )
            }

            // ── 4. Quick Action Panel (revealed from right) ──
            if (actionPanelWidth > 0.dp) {
                ActionRevealPanel(
                    panelWidth = actionPanelWidth,
                    onDeleteClick = onDeleteClick
                )
            }
        }

        // ── 5. Gradient wash overlay (drawWithCache avoids recomposition) ──
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
    }
}

private enum class SwipeState {
    NONE, MOODS, ACTIONS
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
private fun ActionRevealPanel(
    panelWidth: Dp,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(panelWidth)
            .fillMaxHeight()
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(48.dp)
                .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Moment",
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
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
