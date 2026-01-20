package com.tunawicara.app.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
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
import com.tunawicara.app.presentation.revoice.MintGreen
import com.tunawicara.app.presentation.revoice.PeachButton
import com.tunawicara.app.presentation.revoice.TurquoiseBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val passwordResetState by viewModel.passwordResetState.collectAsState()
    val email by viewModel.email.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
                viewModel.clearForm()
                viewModel.resetPasswordResetState()
                onNavigateBack()
            }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = DarkText
                )
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Header
        Text(
            text = "Lupa Password?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        
        Text(
            text = "Masukkan email untuk menerima link reset password",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Email") },
            placeholder = { Text("Masukkan email terdaftar") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = TurquoiseBlue)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TurquoiseBlue,
                cursorColor = TurquoiseBlue
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Success/Error Message
        when (val state = passwordResetState) {
            is PasswordResetState.Success -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Link reset password telah dikirim ke email Anda. Silakan cek inbox atau folder spam.",
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp).fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            is PasswordResetState.Error -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.message,
                        color = Color(0xFFD32F2F),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp).fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            else -> {}
        }
        
        // Send Reset Link Button
        Button(
            onClick = { viewModel.sendPasswordResetEmail() },
            enabled = passwordResetState !is PasswordResetState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = PeachButton,
                contentColor = DarkText
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (passwordResetState is PasswordResetState.Loading) {
                CircularProgressIndicator(
                    color = DarkText,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Kirim Link Reset",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Back to Login Link
        TextButton(onClick = {
            viewModel.clearForm()
            viewModel.resetPasswordResetState()
            onNavigateBack()
        }) {
            Text(
                text = "Kembali ke Halaman Login",
                color = TurquoiseBlue,
                fontSize = 14.sp
            )
        }
    }
}
