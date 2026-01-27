package com.tunawicara.app.presentation.revoice

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tunawicara.app.presentation.home.HomeViewModel
import com.tunawicara.app.presentation.navigation.Screen

// Color palette matching the image
val TurquoiseBlue = Color(0xFF5FC3D1)
val MintGreen = Color(0xFF9DD9C8)
val PeachButton = Color(0xFFFFCDB2)
val DarkText = Color(0xFF2D2D2D)

@Composable
fun ReVoiceScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val isRecording by viewModel.isRecording.collectAsState()
    val recordingDuration by viewModel.recordingDuration.collectAsState()
    val lastRecordedFilePath by viewModel.lastRecordedFilePath.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Progress Hari Ini heading
        Text(
            text = "Progress Hari Ini",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Circular Progress Indicator
        CircularProgressWithText(
            progress = 0.75f,
            modifier = Modifier.size(240.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Streak text
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "7-Days Streak",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
            Text(
                text = "🔥",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Play Button (shown only when recording exists)
        if (lastRecordedFilePath != null && !isRecording) {
            PlayButton(
                isPlaying = isPlaying,
                onClick = {
                    if (isPlaying) {
                        viewModel.pausePlayback()
                    } else {
                        viewModel.playRecording()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
        
        // Big Recording Button
        RecordingButton(
            isRecording = isRecording,
            onClick = {
                navController.navigate(Screen.Latihan.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
fun CircularProgressWithText(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Background circle
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color(0xFFE8E8E8),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 32.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        
        // Progress arc
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = MintGreen,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 32.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        
        // Center text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Text(
                text = "Selesai",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        }
    }
}

@Composable
fun RecordingButton(
    isRecording: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(72.dp)
            .shadow(4.dp, RoundedCornerShape(36.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = PeachButton,
            contentColor = DarkText
        ),
        shape = RoundedCornerShape(36.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = if (isRecording) "Stop" else "Start Recording",
                modifier = Modifier.size(28.dp),
                tint = DarkText
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (isRecording) "Berhenti" else "Mulai Berbicara",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Play button for recorded audio
 */
@Composable
fun PlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = TurquoiseBlue
        ),
        shape = RoundedCornerShape(28.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, TurquoiseBlue)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                modifier = Modifier.size(24.dp),
                tint = TurquoiseBlue
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isPlaying) "Jeda" else "Putar Rekaman",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}



