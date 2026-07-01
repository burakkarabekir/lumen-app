package com.bksd.journal.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.Mood
import com.bksd.core.presentation.link.LinkConfirmationDialog
import com.bksd.core.presentation.link.toOpenableWebUrl
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.content_desc_back
import com.bksd.journal.presentation.content_desc_share
import com.bksd.journal.presentation.detail.components.EntryAnalysisCard
import com.bksd.journal.presentation.detail.components.EntryAnalysisLoadingCard
import com.bksd.journal.presentation.detail.components.EntryCrisisCard
import com.bksd.journal.presentation.detail.components.EntrySupportCard
import com.bksd.journal.presentation.detail.components.MomentDetailAttachments
import com.bksd.journal.presentation.detail.components.MomentDetailMoodChip
import com.bksd.journal.presentation.my_moment_fallback
import com.bksd.journal.presentation.util.MomentFormatter
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentAnalysisState
import com.bksd.reflection.domain.model.MomentReflection
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

private val CoverColors = listOf(Color(0xFF7682D6), Color(0xFF9281C6), Color(0xFFCF6F64))

@Composable
fun MomentDetailReadView(
    state: MomentDetailState,
    moment: Moment,
    formatter: MomentFormatter,
    onAction: (MomentDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    var pendingLink by remember { mutableStateOf<String?>(null) }
    val audioUrl =
        moment.attachments.filterIsInstance<AudioAttachment>().firstOrNull()?.remoteUrl?.value
    val wordCount = moment.body?.trim()?.split(Regex("\\s+"))?.count { it.isNotBlank() } ?: 0

    Box(modifier = modifier.fillMaxSize().background(palette.pageBg)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(264.dp)
                    .background(Brush.linearGradient(CoverColors))
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                0.55f to Color.Transparent,
                                1f to palette.pageBg
                            )
                        )
                )
                val locationName = moment.location?.displayName
                if (!locationName.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 20.dp, bottom = 40.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = locationName,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(palette.pageBg)
                    .padding(horizontal = 22.dp)
                    .padding(top = 24.dp, bottom = 140.dp)
            ) {
                Text(
                    text = formatter.formatTime(moment.createdAt),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = palette.sub
                )
                Spacer(Modifier.height(7.dp))
                Text(
                    text = moment.title.ifEmpty { stringResource(Res.string.my_moment_fallback) },
                    fontSize = 30.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = palette.text
                )

                if (moment.moods.isNotEmpty()) {
                    Spacer(Modifier.height(14.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        moment.moods.forEach { MomentDetailMoodChip(it) }
                    }
                }

                if (!moment.body.isNullOrBlank()) {
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = moment.body!!,
                        fontSize = 16.sp,
                        lineHeight = 26.sp,
                        color = palette.bodyText
                    )
                }

                when (val analysis = state.analysis) {
                    MomentAnalysisState.Pending -> {
                        Spacer(Modifier.height(22.dp))
                        EntryAnalysisLoadingCard()
                    }

                    is MomentAnalysisState.Ready -> {
                        Spacer(Modifier.height(22.dp))
                        when (val reflection = analysis.reflection) {
                            is MomentReflection.Reflection -> EntryAnalysisCard(reflection)
                            is MomentReflection.Support -> EntrySupportCard(reflection)
                            is MomentReflection.Crisis -> EntryCrisisCard(reflection)
                        }
                    }

                    MomentAnalysisState.Failed,
                    MomentAnalysisState.None -> Unit
                }

                if (moment.attachments.isNotEmpty()) {
                    Spacer(Modifier.height(26.dp))
                    MomentDetailAttachments(
                        attachments = moment.attachments,
                        audioPlaybackState = state.audioPlaybackState,
                        audioCurrentPosition = state.audioPositionFormatted,
                        audioDuration = state.audioDurationFormatted,
                        audioAmplitudes = state.audioAmplitudes,
                        onAudioPlayClick = {
                            audioUrl?.let { onAction(MomentDetailAction.OnAudioPlayClick(it)) }
                        },
                        onAudioPauseClick = { onAction(MomentDetailAction.OnAudioPauseClick) },
                        formatter = formatter,
                        onLinkClick = { pendingLink = it }
                    )
                }

                Spacer(Modifier.height(22.dp))
                HorizontalDivider(color = palette.hairline)
                Spacer(Modifier.height(14.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "$wordCount words",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.sub
                    )
                    Text(text = "·", color = palette.sub)
                    Text(
                        text = formatter.formatTime(moment.createdAt),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.sub
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 18.dp, top = 8.dp)
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0x57141420))
                .clickable { onAction(MomentDetailAction.OnNavigateBack) }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(Res.string.content_desc_back),
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 18.dp, top = 8.dp)
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0x57141420))
                .clickable { onAction(MomentDetailAction.OnShareClick) }
        ) {
            Icon(
                imageVector = Icons.Default.IosShare,
                contentDescription = stringResource(Res.string.content_desc_share),
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        pendingLink?.let { url ->
            LinkConfirmationDialog(
                url = url,
                onConfirm = {
                    toOpenableWebUrl(url)?.let { safe -> runCatching { uriHandler.openUri(safe) } }
                    pendingLink = null
                },
                onDismiss = { pendingLink = null }
            )
        }
    }
}

@Preview
@Composable
private fun MomentDetailReadViewPreview() {
    AppTheme(darkTheme = true) {
        MomentDetailReadView(
            state = MomentDetailState(
                analysis = MomentAnalysisState.Ready(
                    reflection = MomentReflection.Support(
                        analysis = EntryAnalysis(
                            summary = "Calm, grateful morning.",
                            moodValence = com.bksd.reflection.domain.model.MoodValence.POSITIVE,
                            moodConfidence = 0.82,
                            dominantEmotions = listOf("calm", "gratitude"),
                            themes = listOf("Calm", "Mornings", "Stillness"),
                            distress = com.bksd.reflection.domain.model.DistressLevel.NONE,
                            distressRationale = ""
                        ),
                        message = "It sounds like things feel heavy right now, and that's hard. You don't have to carry it alone — reaching out to someone you trust can make a difference.",
                        mentalHealthLines = listOf()
                    )
                )
            ),
            moment = Moment(
                id = "1",
                title = "Morning pages",
                body = "Woke up before the alarm and watched the fog roll off the hills. Coffee on the balcony, no phone — just the sound of the neighborhood waking up.",
                createdAt = Clock.System.now(),
                moods = listOf(Mood.CALM, Mood.GRATEFUL, Mood.PROUD),
                location = LocationData(0.0, 0.0, "Mountain View, California")
            ),
            formatter = object : MomentFormatter {
                override fun formatTime(instant: Instant): String =
                    "Saturday, June 27 · 9:41 AM"

                override fun formatDuration(ms: Long): String = "0:42"
            },
            onAction = {}
        )
    }
}
