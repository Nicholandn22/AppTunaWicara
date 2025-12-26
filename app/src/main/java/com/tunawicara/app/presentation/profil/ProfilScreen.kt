package com.tunawicara.app.presentation.profil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tunawicara.app.presentation.revoice.DarkText
import com.tunawicara.app.presentation.revoice.MintGreen
import com.tunawicara.app.presentation.revoice.TurquoiseBlue

/**
 * Profil Screen - User profile and account information
 */
@Composable
fun ProfilScreen() {
    // State for profile data (in real app, would come from ViewModel)
    var username by remember { mutableStateOf("User1") }
    var fullName by remember { mutableStateOf("User 1") }
    var birthDate by remember { mutableStateOf("DD/MM/YYYY") }
    var phoneNumber by remember { mutableStateOf("083274619012") }
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        // Header Section
        Text(
            text = "Profil Pengguna",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp)
        )
        
        // Profile Photo Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Photo Circle
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(4.dp, TurquoiseBlue, CircleShape)
                    .clickable { /* TODO: Open image picker */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Photo",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Gray
                )
            }
            
            // Change Photo Text Button
            TextButton(
                onClick = { /* TODO: Open image picker */ },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = TurquoiseBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Ubah foto profil",
                    color = TurquoiseBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        // Form Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Username Field
            ProfileTextField(
                label = "Username",
                value = username,
                onValueChange = { username = it },
                icon = Icons.Default.AccountCircle,
                placeholder = "Masukkan username",
                enabled = false // Username typically shouldn't be editable
            )
            
            // Full Name Field
            ProfileTextField(
                label = "Nama",
                value = fullName,
                onValueChange = { fullName = it },
                icon = Icons.Default.Person,
                placeholder = "Masukkan nama lengkap"
            )
            
            // Birth Date Field
            ProfileTextField(
                label = "Tanggal Lahir",
                value = birthDate,
                onValueChange = { birthDate = it },
                icon = Icons.Default.Cake,
                placeholder = "DD/MM/YYYY",
                helperText = "Contoh: 01/01/2000"
            )
            
            // Phone Number Field
            ProfileTextField(
                label = "No Telepon",
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                icon = Icons.Default.Phone,
                placeholder = "08xxxxxxxxxx",
                helperText = "Awali dengan 08"
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Save Button
        Button(
            onClick = { /* TODO: Save profile data */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TurquoiseBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Simpan Perubahan",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Logout Button
        OutlinedButton(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFE57373)
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE57373))
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Log Out",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
    
    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Keluar dari Akun?",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin keluar dari akun? Anda perlu login kembali untuk mengakses aplikasi.",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        // TODO: Handle logout logic
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE57373)
                    )
                ) {
                    Text("Ya, Keluar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Batal")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

/**
 * Reusable Profile Text Field Component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    helperText: String? = null
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DarkText,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
        }
        
        // Text Field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TurquoiseBlue,
                unfocusedBorderColor = Color.LightGray,
                disabledBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = MintGreen.copy(alpha = 0.1f),
                disabledContainerColor = Color.LightGray.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 15.sp,
                color = DarkText
            )
        )
        
        // Helper Text
        if (helperText != null) {
            Text(
                text = helperText,
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
