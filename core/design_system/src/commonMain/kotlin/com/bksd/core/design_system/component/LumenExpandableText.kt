package com.bksd.core.design_system.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.Res
import com.bksd.core.design_system.show_less
import com.bksd.core.design_system.show_more
import com.bksd.core.design_system.theme.AppTheme
import org.jetbrains.compose.resources.stringResource


private const val ELLIPSIS = "..."
private const val SPACE_BUFFER = " "

@Composable
fun LumenExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 3
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isExpandable by remember { mutableStateOf(false) }
    var cutOffIndex by remember { mutableIntStateOf(0) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val clickableText = stringResource(if (isExpanded) Res.string.show_less else Res.string.show_more)

    val displayText = remember(text, isExpandable, isExpanded, cutOffIndex, clickableText) {
        buildAnnotatedString {
            when {
                isExpandable && !isExpanded -> {
                    if (cutOffIndex > 0 && cutOffIndex <= text.length) {
                        val truncatedText = text.substring(startIndex = 0, endIndex = cutOffIndex)
                            .trimEnd()
                        append(truncatedText)
                        append(ELLIPSIS)
                        append(SPACE_BUFFER)
                        withStyle(
                            style = SpanStyle(
                                color = primaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(clickableText)
                        }
                    } else {
                        append(text)
                    }
                }
                !isExpanded -> append(text)

                else -> {
                    append(text)
                    append(" ")
                    withStyle(
                        style = SpanStyle(
                            color = primaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(clickableText)
                    }
                }
            }
        }
    }

    Text(
        text = displayText,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = isExpandable,
                interactionSource = null, // to disable ripple effect
                indication = null
            ) { isExpanded = !isExpanded }
            .animateContentSize(),
        onTextLayout = { result ->
            if (!isExpanded && result.hasVisualOverflow) {
                isExpandable = true
                val lastVisibleLineIndex = collapsedMaxLines - 1
                if (result.lineCount > lastVisibleLineIndex && lastVisibleLineIndex >= 0) {
                    val endIndexOfLastLine = result.getLineEnd(lastVisibleLineIndex)
                    val textToDisplayBeforeShowMore = text.substring(0, endIndexOfLastLine)
                    val showMoreAffix = "$ELLIPSIS $clickableText"
                    var potentialCutOff = textToDisplayBeforeShowMore.length - 1
                    while (potentialCutOff > 0 &&
                        (textToDisplayBeforeShowMore
                            .substring(
                                0,
                                potentialCutOff
                            ) + showMoreAffix).length > textToDisplayBeforeShowMore.length
                    ) {
                        potentialCutOff--
                    }
                    cutOffIndex = textToDisplayBeforeShowMore.substring(
                        0,
                        (textToDisplayBeforeShowMore.length - showMoreAffix.length).coerceAtLeast(0)
                    ).trimEnd().length
                } else {
                    isExpandable = false
                }
            }
            if (isExpanded && result.hasVisualOverflow) {
                isExpandable = true
            }
        },
        maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        LumenExpandableText(
            text = buildString {
                repeat(30) {
                    append("Hello ")
                }
            })
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        LumenExpandableText(
            text = buildString {
                repeat(30) {
                    append("Hello ")
                }
            }
        )
    }
}