package com.bksd.journal.presentation.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.content_desc_back
import com.bksd.journal.presentation.content_desc_edit
import com.bksd.journal.presentation.journal.components.AudioPreview
import com.bksd.journal.presentation.journal.components.LinkPreview
import com.bksd.journal.presentation.journal.components.MoodTag
import com.bksd.journal.presentation.journal.components.VideoPreview
import com.bksd.journal.presentation.moment_detail_title
import com.bksd.journal.presentation.my_moment_fallback
import com.bksd.journal.presentation.util.MomentFormatter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MomentDetailRoot(
    momentId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
) {
    val viewModel = koinViewModel<MomentDetailViewModel>(parameters = { parametersOf(momentId) })
    val formatter = koinInject<MomentFormatter>()
    val state by viewModel.state.collectAsState()
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MomentDetailEvent.NavigateBack -> onNavigateBack()
            is MomentDetailEvent.NavigateToEdit -> onNavigateToEdit(event.momentId)
            is MomentDetailEvent.ShowError -> {
                println("Moment Detail Error: ${event.error}")
            }
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
    AppSurface(
        header = {
            AppTopBar(
                title = stringResource(Res.string.moment_detail_title),
                style = AppBarStyle.Root,
                titleContent = {
                    IconButton(onClick = { onAction(MomentDetailAction.OnNavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.content_desc_back)
                        )
                    }
                    Text(
                        text = stringResource(Res.string.moment_detail_title),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                actions = {
                    if (state.moment != null) {
                        IconButton(onClick = { onAction(MomentDetailAction.OnEditClick) }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(Res.string.content_desc_edit))
                        }
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (state.error != null) {
                Text(
                    text = state.error.asString(),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 24.dp)
                )
            } else if (state.moment != null) {
                val moment = state.moment
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Title (Replaced by Body Excerpt)
                    val safeBody = moment.body ?: ""
                    val displayText =
                        safeBody.take(50).let { if (safeBody.length > 50) "$it..." else it }
                    Text(
                        text = displayText.ifEmpty { stringResource(Res.string.my_moment_fallback) },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Metadata Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Feb 13, 2024 at 10:45 AM", // Mocked format 
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        MoodTag(mood = moment.mood)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Body
                    val body = moment.body
                    if (body != null) {
                        Text(
                            text = body,
                            fontSize = 18.sp,
                            lineHeight = 28.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Media
                    val attachments = moment.attachments
                    if (attachments.isNotEmpty()) {
                        when (val primary = attachments.first()) {
                            is PhotoAttachment -> {
                                DetailPhotoPreview(url = primary.remoteUrl.value)
                            }

                            is VideoAttachment -> {
                                VideoPreview(
                                    durationMs = primary.durationMs,
                                    formatter = formatter
                                )
                            }

                            is AudioAttachment -> {
                                AudioPreview(
                                    playbackState = PlaybackState.STOPPED,
                                    currentPositionFormatted = "0:00",
                                    durationFormatted = if (primary.durationMs != null && primary.durationMs > 0)
                                        formatter.formatDuration(primary.durationMs) else "0:00",
                                    onPlayClick = {},
                                    onPauseClick = {}
                                )
                            }

                            is LinkAttachment -> {
                                LinkPreview(url = primary.url.value)
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Tags
                    if (moment.tags.isNotEmpty()) {
                        Row {
                            moment.tags.forEach { tag ->
                                Text(
                                    text = "#$tag",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailPhotoPreview(url: String) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = "Photo",
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .aspectRatio(4f / 3f),
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Photo placeholder",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    )
}
