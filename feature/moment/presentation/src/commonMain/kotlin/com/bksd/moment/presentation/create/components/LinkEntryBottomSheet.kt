package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.dimens
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.add_link
import com.bksd.moment.presentation.done
import com.bksd.moment.presentation.link_url_hint
import org.jetbrains.compose.resources.stringResource

/**
 * Modal bottom sheet for adding a link URL.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkEntryBottomSheet(
    linkInput: String,
    onInputChange: (String) -> Unit,
    onDoneClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.spacing.xxl)
                .padding(bottom = MaterialTheme.dimens.spacing.huge)
        ) {
            // Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.sm))
                Text(
                    text = stringResource(Res.string.add_link),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxl))

            // URL Input
            OutlinedTextField(
                value = linkInput,
                onValueChange = onInputChange,
                placeholder = { Text(stringResource(Res.string.link_url_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(MaterialTheme.dimens.radius.md),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxl))

            // Done Button
            AppButton(
                text = stringResource(Res.string.done),
                onClick = onDoneClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = linkInput.isNotBlank(),
                style = AppButtonStyle.PRIMARY
            )
        }
    }
}
