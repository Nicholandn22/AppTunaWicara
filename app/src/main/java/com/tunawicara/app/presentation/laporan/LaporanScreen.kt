package com.tunawicara.app.presentation.laporan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tunawicara.app.presentation.revoice.DarkText
import com.tunawicara.app.presentation.revoice.MintGreen
import com.tunawicara.app.presentation.revoice.TurquoiseBlue

/**
 * Laporan Screen - Shows progress reports and charts
 */
@Composable
fun LaporanScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Grafik Tren Kemajuan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Articulation Accuracy Chart
            ArticulationAccuracyChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(bottom = 32.dp)
            )
            
            // Response Speed Chart
            ResponseSpeedChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
    }
}

/**
 * Line chart showing articulation accuracy over time
 */
@Composable
fun ArticulationAccuracyChart(
    modifier: Modifier = Modifier
) {
    // Sample data (percentage match over weeks)
    val dataPoints = listOf(45f, 52f, 58f, 65f, 70f, 75f, 78f)
    val weeks = listOf("W1", "W2", "W3", "W4", "W5", "W6", "W7")
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Akurasi Artikulasi",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Persentase kecocokan suara dari AI",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val width = size.width
                    val height = size.height
                    val spacing = width / (dataPoints.size - 1)
                    val maxValue = 100f
                    
                    // Draw grid lines
                    for (i in 0..4) {
                        val y = height * (i / 4f)
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    }
                    
                    // Draw line chart
                    val path = Path()
                    dataPoints.forEachIndexed { index, value ->
                        val x = index * spacing
                        val y = height - (height * (value / maxValue))
                        
                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                        
                        // Draw data points
                        drawCircle(
                            color = MintGreen,
                            radius = 6.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                    
                    // Draw the path
                    drawPath(
                        path = path,
                        color = TurquoiseBlue,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
            
            // X-axis labels (weeks)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weeks.forEach { week ->
                    Text(
                        text = week,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * Bar chart showing response speed over time
 */
@Composable
fun ResponseSpeedChart(
    modifier: Modifier = Modifier
) {
    // Sample data (response time in seconds)
    val dataPoints = listOf(4.5f, 4.0f, 3.8f, 3.2f, 2.9f, 2.5f, 2.2f)
    val weeks = listOf("W1", "W2", "W3", "W4", "W5", "W6", "W7")
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Kecepatan Respons",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Waktu respons terhadap stimulus (detik)",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val maxValue = dataPoints.maxOrNull() ?: 5f
                    
                    dataPoints.forEachIndexed { index, value ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Value label
                            Text(
                                text = "${value}s",
                                fontSize = 10.sp,
                                color = DarkText,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            
                            // Bar
                            Box(
                                modifier = Modifier
                                    .width(32.dp)
                                    .fillMaxHeight(value / maxValue)
                                    .background(
                                        color = TurquoiseBlue,
                                        shape = RoundedCornerShape(
                                            topStart = 8.dp,
                                            topEnd = 8.dp
                                        )
                                    )
                            )
                        }
                    }
                }
            }
            
            // X-axis labels (weeks)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                weeks.forEach { week ->
                    Text(
                        text = week,
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}
