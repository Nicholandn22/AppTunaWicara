package com.tunawicara.app.presentation.latihan

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

// Custom Colors based on design
val HeaderBlue = Color(0xFF5B9EE1)
val ButtonGreen = Color(0xFFCDFFC0)
val ButtonRed = Color(0xFFFFB4B4)
val ButtonYellow = Color(0xFFFFEBAF)
val BottomBarBlue = Color(0xFF63B4F6)
val NavButtonOrange = Color(0xFFFFCCAA)
val TextDark = Color(0xFF333333)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatihanScreen(
    viewModel: LatihanViewModel = hiltViewModel(),
    onNavigateToExercise: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val recordingDuration by viewModel.recordingDuration.collectAsState()
    val similarityScore by viewModel.similarityScore.collectAsState()
    
    // Permission launcher for microphone
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggleRecording()
        }
    }
    
    Scaffold(
        topBar = {
            Column {
                // Status Bar area (simulated color match)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HeaderBlue)
                        .statusBarsPadding()
                )
                
                TopAppBar(
                    title = { 
                        Text(
                            "ReVoice",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ) 
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = HeaderBlue,
                        titleContentColor = TextDark
                    )
                )
            }
        },
        bottomBar = {
            if (uiState is LatihanState.Success) {
                BottomNavigationBar(
                    onPrevious = { viewModel.previousMateri() },
                    onNext = { viewModel.nextMateri() },
                    hasPrevious = (uiState as LatihanState.Success).hasPrevious,
                    hasNext = (uiState as LatihanState.Success).hasNext
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Light gray background
                .padding(paddingValues)
        ) {
            when (val currentState = uiState) {
                is LatihanState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is LatihanState.Success -> {
                    MateriContent(
                        latihanState = currentState,
                        isRecording = isRecording,
                        recordingDuration = recordingDuration,
                        similarityScore = similarityScore,
                        onStartRecording = {
                            viewModel.toggleRecordingWithPermission(
                                onPermissionNeeded = {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            )
                        },
                        onStopRecording = {
                            if (isRecording) viewModel.toggleRecording()
                        },
                        onPlayRecording = {
                            viewModel.playRecording()
                        }
                    )
                }
                is LatihanState.Error -> {
                    ErrorContent(
                        message = currentState.message,
                        onRetry = { viewModel.loadMateriWicara() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun MateriContent(
    latihanState: LatihanState.Success,
    isRecording: Boolean,
    recordingDuration: Long,
    similarityScore: Int?,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onPlayRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentMateri = latihanState.currentMateri
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Icon and Header Title Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Using back arrow as "Return" icon visual
                contentDescription = "Kembali ke menu",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .padding(4.dp),
                tint = TextDark
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "Latihan Berbicara",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Image Area
        currentMateri?.let { materi ->
            materi.materi.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = materi.materi.teks,
                    modifier = Modifier
                        .size(240.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            } ?: Box(modifier = Modifier.size(240.dp)) // Placeholder space
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Speaker Icon - Text - Counter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Speaker Button (Visual Only for now)
                IconButton(
                    onClick = { /* TODO: Implement TTS */ },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFAED581), CircleShape) // Light green circle background
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Dengarkan",
                        tint = TextDark
                    )
                }
                
                // Word Text
                Text(
                    text = materi.materi.teks,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = TextDark
                    )
                )
                
                // Counter
                Text(
                    text = "${latihanState.currentIndex + 1}/${latihanState.materiList.size}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TextDark
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                // Mulai Rekam
                ActionButton(
                    text = "Mulai Rekam",
                    icon = Icons.Default.Mic,
                    backgroundColor = ButtonGreen,
                    onClick = onStartRecording,
                    enabled = !isRecording,
                    height = 48.dp
                )
                
                // Berhenti Rekam
                ActionButton(
                    text = "Berhenti Rekam",
                    icon = Icons.Default.Mic, // Using Mic icon for stop visually in design, but distinct color
                    backgroundColor = ButtonRed,
                    onClick = onStopRecording,
                    enabled = isRecording,
                    height = 48.dp
                )
                
                // Hasil Rekaman
                ActionButton(
                    text = "Hasil Rekaman",
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    backgroundColor = ButtonYellow,
                    onClick = onPlayRecording,
                    height = 48.dp
                )
                
                // Progress Comparison Card
                similarityScore?.let { score ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = TextDark
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Progress : $score%",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
    height: androidx.compose.ui.unit.Dp = 56.dp,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = TextDark,
            disabledContainerColor = backgroundColor.copy(alpha = 0.6f),
            disabledContentColor = TextDark.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    hasPrevious: Boolean,
    hasNext: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BottomBarBlue)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Kembali Button
        Button(
            onClick = onPrevious,
            enabled = hasPrevious,
            colors = ButtonDefaults.buttonColors(
                containerColor = NavButtonOrange,
                contentColor = TextDark,
                disabledContainerColor = NavButtonOrange.copy(alpha = 0.6f),
                disabledContentColor = TextDark.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .height(48.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Kembali",
                fontWeight = FontWeight.Bold
            )
        }
        
        // Lanjut Button
        Button(
            onClick = onNext,
            enabled = hasNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = NavButtonOrange,
                contentColor = TextDark,
                disabledContainerColor = NavButtonOrange.copy(alpha = 0.6f),
                disabledContentColor = TextDark.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .height(48.dp)
        ) {
            Text(
                "Lanjut",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Error Illustration/Icon
        Icon(
            imageVector = Icons.Default.Refresh, // Placeholder for an error illustration
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .background(ButtonRed.copy(alpha = 0.2f), CircleShape)
                .padding(16.dp),
            tint = ButtonRed
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Ups, ada kendala!",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonGreen,
                contentColor = TextDark
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Coba Lagi",
                fontWeight = FontWeight.Bold
            )
        }
    }
}
