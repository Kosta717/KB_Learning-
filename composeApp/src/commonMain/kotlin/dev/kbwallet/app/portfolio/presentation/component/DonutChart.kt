package dev.kbwallet.app.portfolio.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    colors: List<Color>,
    strokeWidth: Float = 40f
) {
    val total = values.sum()
    if (total == 0f || values.isEmpty()) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = (size.minDimension - strokeWidth) / 2
            val rectSize = Size(radius * 2, radius * 2)
            val topLeft = Offset(center.x - radius, center.y - radius)

            var startAngle = -90f
            values.forEachIndexed { index, value ->
                val sweepAngle = (value / total) * 360f
                drawArc(
                    color = colors.getOrElse(index) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweepAngle - 2f, // Small gap
                    useCenter = false,
                    topLeft = topLeft,
                    size = rectSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
                startAngle += sweepAngle
            }
        }
    }
}
