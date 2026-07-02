package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens

@Composable
internal fun StreakBlobs() {
    Box(modifier = Modifier.fillMaxSize()) {
        StreakPetals.forEach { petal ->
            Box(
                modifier = Modifier
                    .align(petal.align)
                    .absoluteOffset(x = petal.x.dp, y = petal.y.dp)
                    .size(width = petal.w.dp, height = petal.h.dp)
                    .rotate(petal.rotate)
                    .blur(radius = petal.blur.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .clip(petal.shape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(petal.c1).copy(alpha = petal.alpha),
                                Color(petal.c2).copy(alpha = petal.alpha),
                            )
                        )
                    )
            )
        }
    }
}

private val PetalShapeA = RoundedCornerShape(
    topStartPercent = 50, topEndPercent = 50, bottomEndPercent = 48, bottomStartPercent = 52
)
private val PetalShapeB = RoundedCornerShape(
    topStartPercent = 46, topEndPercent = 54, bottomEndPercent = 52, bottomStartPercent = 48
)
private val PetalEllipse = RoundedCornerShape(percent = 50)

private data class Petal(
    val align: Alignment,
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int,
    val rotate: Float,
    val blur: Int,
    val c1: Long,
    val c2: Long,
    val alpha: Float,
    val shape: Shape,
)

private val StreakPetals = listOf(
    Petal(Alignment.TopStart, 34, -8, 134, 54, -20f, 4, 0xFFCF8676, 0xFF7A5FB0, 0.55f, PetalShapeA),
    Petal(Alignment.TopStart, -26, 118, 156, 80, 16f, 4, 0xFFB85A52, 0xFF594A8C, 0.55f, PetalShapeB),
    Petal(Alignment.TopEnd, 14, 40, 118, 82, -10f, 6, 0xFF7D6FC0, 0xFF3A3A66, 0.46f, PetalShapeA),
    Petal(Alignment.BottomEnd, -34, 14, 100, 50, 22f, 4, 0xFFC2685E, 0xFF6A5398, 0.48f, PetalEllipse),
    Petal(Alignment.TopEnd, -56, 6, 74, 40, -28f, 3, 0xFF6F74D6, 0xFF9088E0, 0.55f, PetalShapeA),
    Petal(Alignment.Center, -30, 28, 94, 50, -12f, 5, 0xFFC87060, 0xFF8A5FA8, 0.34f, PetalShapeB),
    Petal(Alignment.BottomStart, 16, -16, 88, 46, 24f, 4, 0xFFC2685E, 0xFF6A5398, 0.50f, PetalEllipse),
    Petal(Alignment.CenterEnd, 14, -24, 52, 86, 16f, 5, 0xFF6F63C0, 0xFF3A3A66, 0.44f, PetalShapeA),
    Petal(Alignment.TopStart, 150, 66, 62, 32, -34f, 3, 0xFFD98A78, 0xFFB85A52, 0.52f, PetalEllipse),
    Petal(Alignment.BottomEnd, 34, -34, 64, 36, -18f, 4, 0xFF7A6FC8, 0xFF4A4A86, 0.44f, PetalShapeB),
)

@Preview
@Composable
private fun StreakBlobsPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(280.dp, 200.dp)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
                .background(Brush.verticalGradient(listOf(Color(0xFF30344F), Color(0xFF191B29))))
        ) {
            StreakBlobs()
        }
    }
}
