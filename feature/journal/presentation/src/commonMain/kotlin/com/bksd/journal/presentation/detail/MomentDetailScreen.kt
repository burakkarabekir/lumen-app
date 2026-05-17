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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.location.LocationData
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.detail.components.DetailBottomActionBar
import com.bksd.journal.presentation.detail.components.DetailHeroSection
import com.bksd.journal.presentation.detail.components.DetailJournalEntryCard
import com.bksd.journal.presentation.detail.components.MomentDetailMetadataRow
import com.bksd.journal.presentation.journal.components.MoodTag
import com.bksd.journal.presentation.my_moment_fallback
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock

@Composable
fun MomentDetailRoot(
    momentId: String,
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<MomentDetailViewModel>(parameters = { parametersOf(momentId) })
    val formatter = koinInject<MomentFormatter>()
    val state by viewModel.state.collectAsState()
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MomentDetailEvent.NavigateBack -> onNavigateBack()
            is MomentDetailEvent.ShowError -> Unit
            is MomentDetailEvent.ShowSuccess -> Unit
        }
    }

    MomentDetailScreen(
        state = state,
        formatter = formatter,
        onAction = viewModel::onAction
    )
}

private val HeroHeight = 280.dp
private const val TITLE_COLLAPSE_THRESHOLD_FACTOR = 0.6f
private const val TOOLBAR_SHOW_THRESHOLD = 0.85f

@Composable
fun MomentDetailScreen(
    state: MomentDetailState,
    formatter: MomentFormatter,
    onAction: (MomentDetailAction) -> Unit
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val heroHeightPx = with(density) { HeroHeight.toPx() }
    val collapseThreshold = heroHeightPx * TITLE_COLLAPSE_THRESHOLD_FACTOR

    val titleCollapseProgress by remember {
        derivedStateOf {
            (scrollState.value / collapseThreshold).coerceIn(0f, 1f)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (state.moment != null) {
                DetailBottomActionBar(
                    isEditing = state.isEditing,
                    isSaving = state.isSaving,
                    onDeleteClick = { onAction(MomentDetailAction.OnDeleteClick) },
                    onEditClick = { onAction(MomentDetailAction.OnEditClick) },
                    onSaveClick = { onAction(MomentDetailAction.OnSaveChanges) },
                    onShareClick = { onAction(MomentDetailAction.OnShareClick) },
                    modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                state.error != null -> {
                    Text(
                        text = state.error.asString(),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 24.dp)
                    )
                }

                state.moment != null -> {
                    MomentDetailContent(
                        state = state,
                        moment = state.moment,
                        formatter = formatter,
                        titleCollapseProgressFn = { titleCollapseProgress },
                        onAction = onAction,
                        modifier = Modifier.verticalScroll(scrollState)
                    )

                    CollapsedToolbar(
                        title = state.moment.title.ifEmpty { stringResource(Res.string.my_moment_fallback) },
                        progressFn = { titleCollapseProgress },
                        showMenuIcon = !state.isEditing,
                        onBackClick = { onAction(MomentDetailAction.OnNavigateBack) },
                        onMenuClick = { onAction(MomentDetailAction.OnMenuClick) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CollapsedToolbar(
    title: String,
    progressFn: () -> Float,
    showMenuIcon: Boolean,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val toolbarHeightPx = with(LocalDensity.current) { 64.dp.toPx() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                val progress = progressFn()
                val toolbarAlpha =
                    ((progress - TOOLBAR_SHOW_THRESHOLD) / (1f - TOOLBAR_SHOW_THRESHOLD))
                        .coerceIn(0f, 1f)
                alpha = toolbarAlpha
                translationY = (1f - toolbarAlpha) * -toolbarHeightPx * 0.3f
            }
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            if (showMenuIcon) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun MomentDetailContent(
    state: MomentDetailState,
    moment: Moment,
    formatter: MomentFormatter,
    titleCollapseProgressFn: () -> Float,
    onAction: (MomentDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        DetailHeroSection(
            heroHeight = HeroHeight,
            showMenuIcon = !state.isEditing,
            onBackClick = { onAction(MomentDetailAction.OnNavigateBack) },
            onMenuClick = { onAction(MomentDetailAction.OnMenuClick) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            MomentDetailMetadataRow(
                formattedDateTime = formatter.formatTime(moment.createdAt),
                locationName = moment.location?.displayName
            )

            if (moment.moods.isNotEmpty() && !state.isEditing) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    moment.moods.forEach { mood ->
                        MoodTag(mood = mood)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isEditing) {
                TextField(
                    value = state.editTitle,
                    onValueChange = { onAction(MomentDetailAction.OnTitleChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.my_moment_fallback),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                )
            } else {
                Text(
                    text = moment.title.ifEmpty { stringResource(Res.string.my_moment_fallback) },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.graphicsLayer {
                        val progress = titleCollapseProgressFn()
                        alpha = 1f - progress
                        scaleX = 1f - (progress * 0.15f)
                        scaleY = 1f - (progress * 0.15f)
                        translationY = -progress * 40f
                    }
                )
            }

            val locationName = moment.location?.displayName
            if (!locationName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = locationName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val bodyText = if (state.isEditing) state.editBody else moment.body.orEmpty()
            val tags = if (state.isEditing) state.editTags else moment.tags.toPersistentList()

            if (bodyText.isNotEmpty() || state.isEditing) {
                DetailJournalEntryCard(
                    body = bodyText,
                    tags = tags,
                    isExpanded = state.isBodyExpanded,
                    isEditing = state.isEditing,
                    onToggleExpand = { onAction(MomentDetailAction.OnToggleBodyExpand) },
                    onBodyChange = { onAction(MomentDetailAction.OnBodyChange(it)) },
                    onTagRemove = { onAction(MomentDetailAction.OnTagRemove(it)) }
                )
            }

            if (state.isEditing && state.editMoods.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "MOODS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Mood.entries.forEach { mood ->
                        val isSelected = mood in state.editMoods
                        val backgroundColor =
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        val contentColor =
                            if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(backgroundColor)
                                .clickable { onAction(MomentDetailAction.OnMoodToggle(mood)) }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(text = mood.emoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = mood.label,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = contentColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview
@Composable
private fun MomentDetailScreenPreview() {
    val moment = Moment(
        id = "1",
        title = "A Quiet Realization",
        body = "Today was unexpectedly peaceful. After the chaos of the last few weeks, finding this moment of stillness felt like a gift. I realized that I've been pushing too hard without pausing to calibrate my direction.\n\nWalking through the temple grounds, the smell of incense mixed with the damp earth created a sensory anchor.",
        createdAt = Clock.System.now(),
        moods = listOf(Mood.REFLECTIVE),
        tags = listOf("mindfulness", "career"),
        location = LocationData(0.0, 0.0, "Kyoto, Japan")
    )
    val state = MomentDetailState(
        moment = moment,
        isLoading = false
    )

    AppTheme(darkTheme = true) {
        MomentDetailScreen(
            state = state,
            formatter = PreviewFormatter,
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun MomentDetailScreenEditPreview() {
    val moment = Moment(
        id = "1",
        title = "A Quiet Realization",
        body = "Today was unexpectedly peaceful.",
        createdAt = Clock.System.now(),
        moods = listOf(Mood.REFLECTIVE, Mood.CALM),
        tags = listOf("mindfulness", "career"),
        location = LocationData(0.0, 0.0, "Kyoto, Japan")
    )
    val state = MomentDetailState(
        moment = moment,
        isLoading = false,
        isEditing = true,
        editTitle = "A Quiet Realization",
        editBody = "Today was unexpectedly peaceful.",
        editMoods = persistentSetOf(Mood.REFLECTIVE, Mood.CALM),
        editTags = persistentListOf("mindfulness", "career")
    )

    AppTheme(darkTheme = true) {
        MomentDetailScreen(
            state = state,
            formatter = PreviewFormatter,
            onAction = {}
        )
    }
}

private object PreviewFormatter : MomentFormatter {
    override fun formatTime(instant: kotlin.time.Instant): String = "Oct 15, 2023 · 10:42 PM"
    override fun formatDuration(ms: Long): String = "0:42"
}
