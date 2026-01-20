package com.tunawicara.app.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tunawicara.app.presentation.revoice.DarkText
import com.tunawicara.app.presentation.revoice.PeachButton
import com.tunawicara.app.presentation.revoice.TurquoiseBlue

/**
 * Screen for completing user profile after Google Sign-In
 */
@Composable
fun CompleteProfileScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onProfileComplete: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    
    // Handle profile completion success
    LaunchedEffect(authState) {
        if (authState is AuthState.Success && !(authState as AuthState.Success).needsProfileCompletion) {
            onProfileComplete()
            viewModel.resetAuthState()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        // Header
        Text(
            text = "Lengkapi Profil",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        
        Text(
            text = "Silakan lengkapi data profil Anda",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Full Name Field
        OutlinedTextField(
            value = fullName,
            onValueChange = { viewModel.updateFullName(it) },
            label = { Text("Nama Lengkap") },
            placeholder = { Text("Masukkan nama lengkap") },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null, tint = TurquoiseBlue)
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TurquoiseBlue,
                cursorColor = TurquoiseBlue
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Phone Number Field
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            label = { Text("Nomor Telepon") },
            placeholder = { Text("08xxxxxxxxxx") },
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = null, tint = TurquoiseBlue)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TurquoiseBlue,
                cursorColor = TurquoiseBlue
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Error Message
        if (authState is AuthState.Error) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = Color(0xFFD32F2F),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Save Button
        Button(
            onClick = { viewModel.completeProfile() },
            enabled = authState !is AuthState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = PeachButton,
                contentColor = DarkText
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(
                    color = DarkText,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Simpan & Lanjutkan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
