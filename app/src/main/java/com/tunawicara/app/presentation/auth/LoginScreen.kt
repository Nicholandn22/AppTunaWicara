package com.tunawicara.app.presentation.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.tunawicara.app.presentation.revoice.DarkText
import com.tunawicara.app.presentation.revoice.PeachButton
import com.tunawicara.app.presentation.revoice.TurquoiseBlue

// Web Client ID from Firebase Console
private const val WEB_CLIENT_ID = "281337225766-9m5a3e6f0c6ama91lglcs8ddm2om8tn5.apps.googleusercontent.com"

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToSignup: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    onLoginSuccess: () -> Unit,
    onNeedsProfileCompletion: () -> Unit = {}
) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    
    // Configure Google Sign-In
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }
    
    // Activity result launcher for Google Sign-In
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModel.signInWithGoogle(credential)
            } catch (e: ApiException) {
                Log.e("LoginScreen", "Google sign-in failed", e)
                viewModel.setGoogleSignInError("Login Google gagal: ${e.statusCode}")
            }
        } else {
            Log.d("LoginScreen", "Google sign-in cancelled or failed: ${result.resultCode}")
        }
    }
    
    // Handle login success
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            val state = authState as AuthState.Success
            if (state.needsProfileCompletion) {
                onNeedsProfileCompletion()
            } else {
                onLoginSuccess()
            }
            viewModel.resetAuthState()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        // App Title
        Text(
            text = "ReVoice",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = TurquoiseBlue
        )
        
        Text(
            text = "Tuna Wicara",
            fontSize = 20.sp,
            color = DarkText,
            modifier = Modifier.padding(top = 4.dp)
        )
        
        Spacer(modifier = Modifier.height(80.dp))
        
        // Welcome text
        Text(
            text = "Selamat Datang!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        
        Text(
            text = "Masuk untuk melanjutkan",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
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
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Google Sign-In Button
        Button(
            onClick = {
                // Sign out first to ensure account picker shows
                googleSignInClient.signOut().addOnCompleteListener {
                    launcher.launch(googleSignInClient.signInIntent)
                }
            },
            enabled = authState !is AuthState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = DarkText
            ),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(
                    color = TurquoiseBlue,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Google Icon
                    Text(
                        text = "G",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDB4437)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Masuk dengan Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Footer
        Text(
            text = "Dengan masuk, Anda menyetujui\nSyarat & Ketentuan kami",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}
