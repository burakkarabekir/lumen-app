package com.bksd.journal.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.journal.presentation.journal.components.MediaPlaceholder
import com.bksd.journal.presentation.journal.components.MoodTag
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MomentDetailRoot(
    momentId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
) {
    val viewModel = koinViewModel<MomentDetailViewModel>(parameters = { parametersOf(momentId) })
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
        state = viewModel._state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomentDetailScreen(
    state: MomentDetailState,
    onAction: (MomentDetailAction) -> Unit
) {
    AppSurface(
        header = {
            AppTopBar(
                title = "Moment",
                style = AppBarStyle.Root,
                titleContent = {
                    IconButton(onClick = { onAction(MomentDetailAction.OnNavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = "Moment",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                actions = {
                    if (state.moment != null) {
                        IconButton(onClick = { onAction(MomentDetailAction.OnEditClick) }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
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
                        text = displayText.ifEmpty { "My Moment" },
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
                        MediaPlaceholder(type = attachments.first().type)
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
