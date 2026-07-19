package com.bksd.journal.presentation.detail.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.ShareStyle
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.shareSuccessGreen
import com.bksd.core.presentation.share.encodeToPngBytes
import com.bksd.core.presentation.share.rememberShareImageActions
import com.bksd.journal.presentation.model.MomentUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val EXPORT_WIDTH_PX = 1080f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareMomentSheet(
    moment: MomentUi,
    dateLabel: String,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var format by remember { mutableStateOf(ShareFormat.PORTRAIT) }
    var style by remember { mutableStateOf(ShareStyle.AURORA) }
    var showDate by remember { mutableStateOf(true) }
    var showLocation by remember { mutableStateOf(moment.location != null) }
    var showMood by remember { mutableStateOf(moment.moods.isNotEmpty()) }
    var status by remember { mutableStateOf<String?>(null) }

    val captureLayer = rememberGraphicsLayer()
    val shareActions = rememberShareImageActions()
    val scope = rememberCoroutineScope()
    val exportWidthDp = (EXPORT_WIDTH_PX / LocalDensity.current.density).dp

    fun capture(action: (ByteArray) -> Unit) {
        scope.launch {
            val png = captureLayer.toImageBitmap().encodeToPngBytes()
            action(png)
        }
    }

    LaunchedEffect(status) {
        if (status != null) {
            delay(2200)
            status = null
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Box {
            ShareMomentSheetContent(
                moment = moment,
                dateLabel = dateLabel,
                format = format,
                style = style,
                showDate = showDate,
                showLocation = showLocation,
                showMood = showMood,
                onFormat = { format = it },
                onStyle = { style = it },
                onToggleDate = { showDate = it },
                onToggleLocation = { showLocation = it },
                onToggleMood = { showMood = it },
                onSystemShare = { capture { shareActions.share(it) } },
                onSaveImage = { capture { if (shareActions.save(it)) status = "Saved to your photos" } },
                onCopy = { capture { if (shareActions.copy(it)) status = "Copied to clipboard" } },
                onDismiss = onDismiss,
            )

            Box(modifier = Modifier.size(0.dp).wrapContentSize(align = Alignment.TopStart, unbounded = true)) {
                ShareableMomentCard(
                    moment = moment,
                    style = style,
                    format = format,
                    showDate = showDate,
                    showLocation = showLocation,
                    showMood = showMood,
                    dateLabel = dateLabel,
                    modifier = Modifier
                        .requiredWidth(exportWidthDp)
                        .drawWithContent { captureLayer.record { this@drawWithContent.drawContent() } }
                )
            }

            status?.let { message ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = MaterialTheme.dimens.spacing.xxxl)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.full))
                        .background(MaterialTheme.colorScheme.inverseSurface)
                        .padding(horizontal = MaterialTheme.dimens.spacing.xl, vertical = MaterialTheme.dimens.spacing.md)
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.extended.shareSuccessGreen, modifier = Modifier.size(MaterialTheme.dimens.icon.sm))
                    Text(text = message, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.inverseOnSurface)
                }
            }
        }
    }
}
