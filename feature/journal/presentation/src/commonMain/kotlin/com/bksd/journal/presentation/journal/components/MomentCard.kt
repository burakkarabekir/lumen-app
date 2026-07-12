@file:OptIn(ExperimentalMaterial3Api::class)

package com.bksd.journal.presentation.journal.components
import com.bksd.core.presentation.labelRes

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
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.journal.presentation.model.MomentUi
import com.bksd.core.domain.model.Mood
import com.bksd.core.domain.model.PlaybackState
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.content_desc_favorited
import com.bksd.journal.presentation.content_desc_more_actions
import com.bksd.journal.presentation.util.DefaultMomentFormatter
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt
import kotlin.time.Clock

@Composable
fun MomentCard(
    moment: MomentUi,
    formatter: MomentFormatter,
    timeZone: TimeZone,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    audioPlaybackState: PlaybackState = PlaybackState.STOPPED,
    audioCurrentPosition: String = "0:00",
    onAudioPlayClick: () -> Unit = {},
    onAudioPauseClick: () -> Unit = {},
    onLinkClick: (String) -> Unit = {},
    onEditClick: () -> Unit = {},
    onFavoriteToggleClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val palette = rememberNewEntryPalette()
    val extendedColors = MaterialTheme.colorScheme.extended
    val formattedDate = remember(moment.createdAt) { formatCardDate(moment.createdAt, timeZone) }

    val hasMoods = moment.moods.isNotEmpty()
    var moodPanelExpanded by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = moodPanelExpanded, label = "MoodReveal")

    val moodColorList = remember(moment.moods, extendedColors) {
        moment.moods.map { moodColors(it, extendedColors).second.copy(alpha = 0.5f) }
    }
    val primaryMoodColor = moodColorList.firstOrNull() ?: Color.Transparent
    val accentColor = remember(moment.moods, extendedColors, palette.saveBg) {
        moment.moods.firstOrNull()?.let { moodColors(it, extendedColors).second } ?: palette.saveBg
    }

    val panelWidth by transition.animateDp(
        transitionSpec = {
            tween(
                MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "PanelWidth"
    ) { expanded -> if (expanded) MomentCardDefaults.PANEL_EXPANDED_WIDTH_DP.dp else 0.dp }

    val contentBlur by transition.animateDp(
        transitionSpec = {
            tween(
                MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "ContentBlur"
    ) { expanded -> if (expanded) 6.dp else 0.dp }

    val gradientAlpha by transition.animateFloat(
        transitionSpec = {
            tween(
                MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "GradientAlpha"
    ) { expanded -> if (expanded) 0.15f else 0f }

    val revealProgress by transition.animateFloat(
        transitionSpec = {
            tween(
                MomentCardDefaults.REVEAL_DURATION_MS,
                easing = MomentCardDefaults.RevealEasing
            )
        },
        label = "RevealProgress"
    ) { expanded -> if (expanded) 1f else 0f }

    var isActionsSheetVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .background(palette.surface)
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
        Row(modifier = Modifier.fillMaxWidth()) {
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
                    .then(if (contentBlur > 0.dp) Modifier.blur(contentBlur) else Modifier)
                    .clickable { onClick() }
            ) {
                val locationName = moment.location?.displayName
                if (!locationName.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(palette.pinBg)
                            .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = palette.pinFg,
                            modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
                        )
                        Text(
                            text = locationName,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.pinFg,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Column(modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.lg, end = MaterialTheme.dimens.spacing.lg, top = MaterialTheme.dimens.spacing.lg)) {
                    Text(
                        text = moment.title,
                        fontSize = 19.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.3).sp,
                        color = palette.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val body = moment.body
                    if (!body.isNullOrBlank()) {
                        Text(
                            text = body,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = palette.sub,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xs)
                        )
                    }
                }

                if (moment.attachments.isNotEmpty()) {
                    AttachmentChips(
                        attachments = moment.attachments,
                        accentColor = accentColor,
                        formatter = formatter,
                        playbackState = audioPlaybackState,
                        onAudioPlayClick = onAudioPlayClick,
                        onAudioPauseClick = onAudioPauseClick,
                        onLinkClick = onLinkClick,
                        modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.md)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimens.spacing.lg, start = MaterialTheme.dimens.spacing.lg, end = MaterialTheme.dimens.spacing.lg)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(palette.hairline)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = MaterialTheme.dimens.spacing.lg, end = MaterialTheme.dimens.spacing.sm, top = MaterialTheme.dimens.spacing.xs, bottom = MaterialTheme.dimens.spacing.sm),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formattedDate,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.sub
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xxs)
                    ) {
                        if (moment.isFavorite) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = stringResource(Res.string.content_desc_favorited),
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
                            )
                        }
                        IconButton(
                            onClick = { isActionsSheetVisible = true },
                            modifier = Modifier.size(MaterialTheme.dimens.icon.avatar)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = stringResource(Res.string.content_desc_more_actions),
                                tint = palette.sub,
                                modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
                            )
                        }
                    }
                }
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
            Box(modifier = Modifier.matchParentSize()) {
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
            .clip(RoundedCornerShape(topStart = MaterialTheme.dimens.radius.xl, bottomStart = MaterialTheme.dimens.radius.xl))
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
            .padding(vertical = MaterialTheme.dimens.spacing.md, horizontal = MaterialTheme.dimens.spacing.sm),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs)
    ) {
        moods.forEachIndexed { index, mood ->
            val staggerDelay = index * 0.12f
            val chipAlpha = ((revealProgress - staggerDelay) / 0.35f).coerceIn(0f, 1f)

            val (bg, textColor) = moodColors(mood, extendedColors)
            Row(
                modifier = Modifier
                    .graphicsLayer { alpha = chipAlpha }
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                    .background(bg)
                    .padding(horizontal = MaterialTheme.dimens.spacing.sm, vertical = MaterialTheme.dimens.spacing.xs),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = mood.emoji, fontSize = 12.sp)
                Text(
                    text = stringResource(mood.labelRes()),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMomentCard() {
    AppTheme(darkTheme = true) {
        MomentCard(
            moment = MomentUi(
                id = "0",
                title = "A quiet morning walk",
                body = "The air was crisp and the sun had just begun to rise.",
                createdAt = Clock.System.now(),
                moods = persistentListOf(Mood.CALM, Mood.REFLECTIVE),
                tags = persistentListOf(),
                attachments = persistentListOf(),
                location = null,
                isFavorite = false,
            ),
            formatter = DefaultMomentFormatter(TimeZone.UTC),
            onClick = {},
            timeZone = TimeZone.UTC
        )
    }
}

@Preview
@Composable
private fun PreviewMomentCardLight() {
    AppTheme(darkTheme = false) {
        MomentCard(
            moment = MomentUi(
                id = "0",
                title = "Creative burst at midnight",
                body = "Ideas flowing like a river. Wrote three pages without stopping.",
                createdAt = Clock.System.now(),
                moods = persistentListOf(Mood.INSPIRED),
                tags = persistentListOf(),
                attachments = persistentListOf(),
                location = null,
                isFavorite = false,
            ),
            formatter = DefaultMomentFormatter(TimeZone.UTC),
            onClick = {},
            timeZone = TimeZone.UTC
        )
    }
}
