package com.bksd.insights.presentation.reflection.full

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.bksd.core.design_system.theme.extended
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.reflection.full.components.EmotionalArcCard
import com.bksd.insights.presentation.reflection.full.components.QuestionsToSitWithCard
import com.bksd.insights.presentation.reflection.full.components.RecurringThemesCard
import com.bksd.insights.presentation.reflection.full.components.StandoutMomentCard
import com.bksd.insights.presentation.reflection.full.components.WeeklySectionLabel
import com.bksd.insights.presentation.weekly_back
import com.bksd.insights.presentation.weekly_based_on_entries
import com.bksd.insights.presentation.weekly_entry_plural
import com.bksd.insights.presentation.weekly_entry_singular
import com.bksd.insights.presentation.weekly_no_reflection
import com.bksd.insights.presentation.weekly_reflection_title
import com.bksd.insights.presentation.weekly_section_questions
import com.bksd.insights.presentation.weekly_section_standout
import com.bksd.insights.presentation.weekly_section_themes
import com.bksd.reflection.domain.model.ArcPoint
import com.bksd.reflection.domain.model.ReflectionTheme
import com.bksd.reflection.domain.model.StandoutEntry
import com.bksd.reflection.domain.model.WeeklyMomentInsights
import com.bksd.reflection.domain.model.WeeklyReflection
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.unit.dp

@Composable
fun WeeklyReflectionDetailRoot(
    onBack: () -> Unit,
    onOpenMoment: (String) -> Unit,
) {
    val viewModel = koinViewModel<WeeklyReflectionDetailViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            WeeklyReflectionDetailEvent.NavigateBack -> onBack()
            is WeeklyReflectionDetailEvent.OpenMoment -> onOpenMoment(event.momentId)
        }
    }

    WeeklyReflectionScreen(state = state, onAction = viewModel::onAction)
}

@Composable
private fun WeeklyReflectionScreen(
    state: WeeklyReflectionDetailState,
    onAction: (WeeklyReflectionDetailAction) -> Unit,
) {
    val palette = rememberNewEntryPalette()
    val scrollState = rememberScrollState()
    val reflection = state.reflection
    val insights = state.insights

    Box(modifier = Modifier.fillMaxSize().background(palette.pageBg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.icon.tile)
                        .clip(CircleShape)
                        .background(palette.surface)
                        .clickable { onAction(WeeklyReflectionDetailAction.OnBack) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(Res.string.weekly_back),
                        tint = palette.text,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.xl)
                    )
                }
                Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
                Text(
                    text = stringResource(Res.string.weekly_reflection_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = palette.text
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.spacing.xl)
                    .padding(top = MaterialTheme.dimens.spacing.sm, bottom = MaterialTheme.dimens.spacing.huge)
            ) {
                if (reflection != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val entryNoun = stringResource(
                            if (reflection.entryCount == 1) Res.string.weekly_entry_singular
                            else Res.string.weekly_entry_plural
                        )
                        Text(
                            text = stringResource(
                                Res.string.weekly_based_on_entries,
                                reflection.entryCount,
                                entryNoun,
                                reflection.rangeLabel
                            ),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = palette.sub,
                            modifier = Modifier.weight(1f)
                        )
                        if (reflection.summary.isNotBlank()) {
                            SummaryPill(reflection.summary)
                        }
                    }
                    Text(
                        text = reflection.narrative,
                        fontSize = 16.sp,
                        lineHeight = 26.sp,
                        color = palette.bodyText,
                        modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg)
                    )
                }

                if (insights != null && insights.arc.any { it.hasEntry }) {
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxl))
                    EmotionalArcCard(arc = insights.arc, brightestDayLabel = insights.brightestDayLabel)
                }

                if (reflection != null && reflection.themes.isNotEmpty()) {
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxl))
                    WeeklySectionLabel(stringResource(Res.string.weekly_section_themes))
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))
                    RecurringThemesCard(themes = reflection.themes)
                }

                val standout = insights?.standout
                if (standout != null) {
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxl))
                    WeeklySectionLabel(stringResource(Res.string.weekly_section_standout))
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))
                    StandoutMomentCard(
                        standout = standout,
                        onOpen = { onAction(WeeklyReflectionDetailAction.OnOpenMoment(standout.momentId)) }
                    )
                }

                if (reflection != null && reflection.questions.isNotEmpty()) {
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxl))
                    WeeklySectionLabel(stringResource(Res.string.weekly_section_questions))
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))
                    QuestionsToSitWithCard(questions = reflection.questions)
                }

                if (reflection == null && !state.isLoading) {
                    Text(
                        text = stringResource(Res.string.weekly_no_reflection),
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = palette.sub,
                        modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xl)
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryPill(text: String) {
    val c = MaterialTheme.colorScheme.extended.reflectionCard
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs),
        modifier = Modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
            .background(c.pillBg)
            .padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.xs)
    ) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(c.pillContent))
        Text(text = text, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = c.pillContent)
    }
}

@Preview
@Composable
private fun WeeklyReflectionScreenPreview() {
    AppTheme(darkTheme = true) {
        WeeklyReflectionScreen(
            state = WeeklyReflectionDetailState(
                reflection = WeeklyReflection(
                    narrative = "This week leaned quiet. Your mornings kept showing up — fog, coffee, the balcony " +
                        "— with a steady thread of gratitude underneath. You wrote most on the days you let yourself slow down.",
                    summary = "Calm week",
                    themes = listOf(
                        ReflectionTheme("Calm", "#2FA876", 5),
                        ReflectionTheme("Gratitude", "#C77FA8", 3),
                        ReflectionTheme("Mornings", "#E0A21A", 3),
                        ReflectionTheme("Rest", "#6E7AD0", 2),
                    ),
                    questions = listOf("What would it take to protect one slow morning each week?"),
                    entryCount = 6,
                    rangeLabel = "Jun 21–27",
                    generatedAtMs = 0L
                ),
                insights = WeeklyMomentInsights(
                    arc = listOf(
                        ArcPoint("M", true, 0.52f, "#2FA876"),
                        ArcPoint("T", true, 0.68f, "#3F9C8D"),
                        ArcPoint("W", true, 0.40f, "#6E7AD0"),
                        ArcPoint("T", true, 0.78f, "#C77FA8"),
                        ArcPoint("F", true, 0.58f, "#2FA876"),
                        ArcPoint("S", true, 0.92f, "#E0A21A"),
                        ArcPoint("S", false, 0f, null),
                    ),
                    brightestDayLabel = "Sat",
                    standout = StandoutEntry("1", "Morning pages", "I want more mornings that start this slowly.", "#C77FA8")
                ),
                isLoading = false
            ),
            onAction = {}
        )
    }
}
