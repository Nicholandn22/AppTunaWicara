package com.tunawicara.app.presentation.trofi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tunawicara.app.presentation.revoice.DarkText
import com.tunawicara.app.presentation.revoice.MintGreen
import com.tunawicara.app.presentation.revoice.TurquoiseBlue

/**
 * Data class representing an achievement
 */
data class Achievement(
    val days: Int,
    val title: String,
    val description: String,
    val isUnlocked: Boolean = false
)

/**
 * Trofi Screen - Shows achievement milestones
 */
@Composable
fun TrofiScreen() {
    // Sample achievements - in real app, this would come from ViewModel
    val achievements = listOf(
        Achievement(1, "Langkah Pertama", "Mulai perjalanan berbicara Anda!", true),
        Achievement(3, "Spora Awal", "Konsisten 3 hari berturut-turut!", true),
        Achievement(7, "Semangat Hebat", "Satu minggu penuh latihan!", false),
        Achievement(15, "Bintang Bicara", "15 hari berlatih dengan giat!", false),
        Achievement(30, "Pejuang Bicara", "Satu bulan penuh dedikasi!", false),
        Achievement(50, "Pencapaian Emas", "50 hari konsistensi luar biasa!", false),
        Achievement(75, "Bicara Konsisten", "75 hari tidak pernah menyerah!", false),
        Achievement(100, "Sang Harapan", "100 hari - Anda luar biasa!", false),
        Achievement(150, "Master Bicara", "150 hari menuju kesempurnaan!", false),
        Achievement(200, "Legenda Bicara", "200 hari - Anda adalah inspirasi!", false)
    )
    
    // Count unlocked achievements
    val unlockedCount = achievements.count { it.isUnlocked }
    val totalCount = achievements.size
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Progress Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Penghargaan Saya",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Kumpulkan penghargaan dengan berlatih setiap hari!",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Progress indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Kemajuan Anda",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        
                        LinearProgressIndicator(
                            progress = unlockedCount.toFloat() / totalCount,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = TurquoiseBlue,
                            trackColor = Color.LightGray.copy(alpha = 0.3f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Text(
                        text = "$unlockedCount/$totalCount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TurquoiseBlue
                    )
                }
            }
        }
        
        // Achievements Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievements) { achievement ->
                AchievementCard(achievement = achievement)
            }
        }
    }
}

/**
 * Individual achievement card with calendar design
 */
@Composable
fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) Color.White else Color.White.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (achievement.isUnlocked) 6.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Calendar-like header with hooks
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        color = Color(0xFF9E9E9E),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Hook circles
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .offset(y = (-2).dp)
                            .background(Color(0xFF757575), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .offset(y = (-2).dp)
                            .background(Color(0xFF757575), CircleShape)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Calendar body with day number
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = achievement.days.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (achievement.isUnlocked) TurquoiseBlue else Color.Gray
                    )
                    
                    Text(
                        text = "Hari",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                
                // Star icon in corner
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Icon(
                        imageVector = if (achievement.isUnlocked) Icons.Default.Star else Icons.Default.StarOutline,
                        contentDescription = if (achievement.isUnlocked) "Unlocked" else "Locked",
                        tint = if (achievement.isUnlocked) MintGreen else Color.LightGray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Achievement title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (achievement.isUnlocked) TurquoiseBlue else Color(0xFF9E9E9E),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = achievement.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
            
            // Lock/Unlock indicator
            if (!achievement.isUnlocked) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Terkunci",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Unlocked",
                        tint = MintGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Terbuka",
                        fontSize = 10.sp,
                        color = MintGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
