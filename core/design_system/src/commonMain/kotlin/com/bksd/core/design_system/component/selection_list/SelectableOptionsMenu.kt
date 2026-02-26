package com.bksd.core.design_system.component.selection_list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.bksd.campusechojournal.core.presentation.design_system.selection_list.SelectableOptionExtras
import com.bksd.core.design_system.Res
import com.bksd.core.design_system.component.selection_list.Selectable.Companion.asUnselectedItems
import com.bksd.core.design_system.create_entry
import com.bksd.core.design_system.hashtag
import com.bksd.core.design_system.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun <T> SelectableOptionsMenu(
    items: ImmutableList<Selectable<T>>,
    itemDisplayText: (T) -> String,
    onDismiss: () -> Unit,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    onItemClick: (Selectable<T>) -> Unit,
    leadingIcon: (@Composable (T) -> Unit)? = null,
    offset: IntOffset = IntOffset.Zero,
    maxHeight: Dp = Dp.Unspecified,
    extras: SelectableOptionExtras? = null,
) {
    val listState = rememberLazyListState()

    Popup(
        onDismissRequest = onDismiss,
        offset = offset,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 4.dp,
            modifier = modifier
                .heightIn(
                    max = maxHeight
                )
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .animateContentSize()
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = items,
                    key = { key(it.item) }
                ) { selectable ->
                    Row(
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                color = if (selectable.selected) {
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.05f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                            .clickable { onItemClick(selectable) }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        leadingIcon?.invoke(selectable.item)
                        Text(
                            text = itemDisplayText(selectable.item),
                            modifier = Modifier.weight(1f)
                        )
                        if (selectable.selected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                if (extras != null && extras.text.isNotEmpty()) {
                    item(key = true) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { extras.onClick() },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(18.dp)
                            )
                            Text(
                                text = stringResource(
                                    Res.string.create_entry,
                                    extras.text
                                ),
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SelectableOptionsMenuPreview() {
    AppTheme {
        SelectableOptionsMenu(
            items = (1..5).map {
                "Selectable Option: $it"
            }.asUnselectedItems().toImmutableList(),
            key = { it },
            itemDisplayText = { it },
            onDismiss = {},
            onItemClick = {},
            leadingIcon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.hashtag),
                    contentDescription = null
                )
            },
            maxHeight = 500.dp,
            extras = SelectableOptionExtras(
                text = "Add Topic",
                onClick = {}
            )
        )
    }
}

@Preview
@Composable
fun SelectableSelectableOptionsPreview() {
    AppTheme {
        SelectableOptionsMenu(
            items = listOf(
                Selectable(
                    item = "Option 1",
                    selected = false
                ),
                Selectable(
                    item = "Option 2",
                    selected = true
                ),
                Selectable(
                    item = "Option 3",
                    selected = true
                )
            ).toImmutableList(),
            key = { it },
            itemDisplayText = { it },
            onDismiss = {},
            onItemClick = {},
            leadingIcon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.hashtag),
                    contentDescription = null
                )
            },
            maxHeight = 500.dp,
            extras = SelectableOptionExtras(
                text = "Add Topic",
                onClick = {}
            )
        )
    }
}