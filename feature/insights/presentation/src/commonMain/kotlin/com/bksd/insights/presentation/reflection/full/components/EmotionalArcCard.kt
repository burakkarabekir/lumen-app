package com.bksd.insights.presentation.reflection.full.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.initialRes
import com.bksd.insights.presentation.reflection.reflectionHexColor
import com.bksd.insights.presentation.shortNameRes
import com.bksd.insights.presentation.weekly_arc_empty
import com.bksd.insights.presentation.weekly_brightest_on
import com.bksd.insights.presentation.weekly_emotional_arc
import com.bksd.reflection.domain.model.ArcPoint
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmotionalArcCard(
    arc: List<ArcPoint>,
    brightestDay: DayOfWeek?,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val accent = palette.saveBg
    val lineColor = if (dark) Color.White.copy(alpha = 0.4f) else accent.copy(alpha = 0.4f)
    val hasData = arc.any { it.hasEntry }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .background(palette.surface)
            .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.lg)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.weekly_emotional_arc),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.7.sp,
                color = palette.sub
            )
            if (brightestDay != null) {
                Text(
                    text = stringResource(Res.string.weekly_brightest_on, stringResource(brightestDay.shortNameRes())),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
            }
        }

        if (!hasData) {
            Text(
                text = stringResource(Res.string.weekly_arc_empty),
                fontSize = 13.sp,
                color = palette.sub,
                modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg, bottom = MaterialTheme.dimens.spacing.xs)
            )
            return@Column
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)
                .padding(top = MaterialTheme.dimens.spacing.md)
        ) {
            val padX = size.width * 0.05f
            val usable = size.width - padX * 2
            val step = usable / 6f
            val topY = size.height * 0.14f
            val baseY = size.height * 0.86f

            val points = arc.mapIndexedNotNull { index, point ->
                if (!point.hasEntry) null else {
                    val x = padX + index * step
                    val y = baseY - point.valence.coerceIn(0f, 1f) * (baseY - topY)
                    Triple(Offset(x, y), point.colorHex?.let(::reflectionHexColor) ?: accent, index)
                }
            }
            if (points.isEmpty()) return@Canvas

            val area = Path().apply {
                moveTo(points.first().first.x, baseY)
                points.forEach { lineTo(it.first.x, it.first.y) }
                lineTo(points.last().first.x, baseY)
                close()
            }
            drawPath(
                path = area,
                brush = Brush.verticalGradient(
                    colors = listOf(accent.copy(alpha = 0.28f), accent.copy(alpha = 0f)),
                    startY = topY,
                    endY = baseY
                )
            )

            if (points.size > 1) {
                val line = Path().apply {
                    moveTo(points.first().first.x, points.first().first.y)
                    points.drop(1).forEach { lineTo(it.first.x, it.first.y) }
                }
                drawPath(line, color = lineColor, style = Stroke(width = 2.5.dp.toPx()))
            }

            points.forEach { (offset, color, _) ->
                drawCircle(color, radius = 4.6.dp.toPx(), center = offset)
                drawCircle(palette.surface, radius = 4.6.dp.toPx(), center = offset, style = Stroke(2.5.dp.toPx()))
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.spacing.sm)
        ) {
            arc.forEach { point ->
                Text(
                    text = stringResource(point.day.initialRes()),
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.sub
                )
            }
        }
    }
}

private fun sampleArc() = listOf(
    ArcPoint(DayOfWeek.MONDAY, true, 0.52f, "#2FA876"),
    ArcPoint(DayOfWeek.TUESDAY, true, 0.68f, "#3F9C8D"),
    ArcPoint(DayOfWeek.WEDNESDAY, true, 0.40f, "#6E7AD0"),
    ArcPoint(DayOfWeek.THURSDAY, true, 0.78f, "#C77FA8"),
    ArcPoint(DayOfWeek.FRIDAY, true, 0.58f, "#2FA876"),
    ArcPoint(DayOfWeek.SATURDAY, true, 0.92f, "#E0A21A"),
    ArcPoint(DayOfWeek.SUNDAY, true, 0.72f, "#2FA876"),
)

@Preview
@Composable
private fun EmotionalArcCardPreview() {
    AppTheme(darkTheme = true) {
        EmotionalArcCard(arc = sampleArc(), brightestDay = DayOfWeek.SATURDAY, modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg))
    }
}
