package com.bksd.journal.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.Mood
import com.bksd.core.presentation.share.rememberShareLauncher
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.journal.presentation.detail.components.DetailBottomActionBar
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock

@Composable
fun MomentDetailRoot(
    momentId: String,
    isEditing: Boolean = false,
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<MomentDetailViewModel>(parameters = { parametersOf(momentId, isEditing) })
    val formatter = koinInject<MomentFormatter>()
    val shareLauncher = rememberShareLauncher()
    val state by viewModel.state.collectAsState()
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MomentDetailEvent.NavigateBack -> onNavigateBack()
            is MomentDetailEvent.ShareEntry -> shareLauncher.share(event.text)
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

@Composable
fun MomentDetailScreen(
    state: MomentDetailState,
    formatter: MomentFormatter,
    onAction: (MomentDetailAction) -> Unit
) {
    val moment = state.moment
    when {
        moment != null -> {
            val palette = rememberNewEntryPalette()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(palette.pageBg)
            ) {
                if (state.isEditing) {
                    MomentDetailEditView(
                        state = state,
                        moment = moment,
                        formatter = formatter,
                        onAction = onAction
                    )
                } else {
                    MomentDetailReadView(
                        state = state,
                        moment = moment,
                        formatter = formatter,
                        onAction = onAction
                    )
                }

                DetailBottomActionBar(
                    isEditing = state.isEditing,
                    isSaving = state.isSaving,
                    isFavorite = moment.isFavorite,
                    onDeleteClick = { onAction(MomentDetailAction.OnDeleteClick) },
                    onEditClick = { onAction(MomentDetailAction.OnEditClick) },
                    onSaveClick = { onAction(MomentDetailAction.OnSaveChanges) },
                    onFavoriteClick = { onAction(MomentDetailAction.OnFavoriteToggle) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        state.error != null -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.error.asString(),
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview
@Composable
private fun MomentDetailScreenReadPreview() {
    val moment = Moment(
        id = "1",
        title = "A Quiet Realization",
        body = "Today was unexpectedly peaceful. After the chaos of the last few weeks, finding this moment of stillness felt like a gift.",
        createdAt = Clock.System.now(),
        moods = listOf(Mood.REFLECTIVE, Mood.CALM),
        tags = listOf("mindfulness"),
        location = LocationData(0.0, 0.0, "Kyoto, Japan")
    )
    AppTheme(darkTheme = true) {
        MomentDetailScreen(
            state = MomentDetailState(moment = moment, isLoading = false),
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
        tags = listOf("mindfulness"),
        location = LocationData(0.0, 0.0, "Kyoto, Japan")
    )
    AppTheme(darkTheme = true) {
        MomentDetailScreen(
            state = MomentDetailState(
                moment = moment,
                isLoading = false,
                isEditing = true,
                editTitle = "A Quiet Realization",
                editBody = "Today was unexpectedly peaceful.",
                editMoods = persistentSetOf(Mood.REFLECTIVE, Mood.CALM),
                editTags = persistentListOf("mindfulness"),
                editLocation = LocationData(0.0, 0.0, "Kyoto, Japan"),
                editCreatedAt = moment.createdAt
            ),
            formatter = PreviewFormatter,
            onAction = {}
        )
    }
}

private object PreviewFormatter : MomentFormatter {
    override fun formatTime(instant: kotlin.time.Instant): String = "Oct 15, 2023 · 10:42 PM"
    override fun formatDuration(ms: Long): String = "0:42"
}
