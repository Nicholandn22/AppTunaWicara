package com.tunawicara.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.tunawicara.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling authentication operations
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _passwordResetState = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(authRepository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    private val _needsProfileCompletion = MutableStateFlow(false)
    val needsProfileCompletion: StateFlow<Boolean> = _needsProfileCompletion.asStateFlow()
    
    // Form states
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()
    
    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()
    
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()
    
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()
    
    fun updateEmail(value: String) { _email.value = value }
    fun updatePassword(value: String) { _password.value = value }
    fun updateConfirmPassword(value: String) { _confirmPassword.value = value }
    fun updateFullName(value: String) { _fullName.value = value }
    fun updatePhoneNumber(value: String) { _phoneNumber.value = value }
    
    init {
        // Check profile completion on init if logged in
        if (authRepository.isLoggedIn()) {
            checkProfileCompletion()
        }
    }
    
    private fun checkProfileCompletion() {
        viewModelScope.launch {
            _needsProfileCompletion.value = !authRepository.isProfileComplete()
        }
    }
    
    /**
     * Login with email and password
     */
    fun login() {
        if (!validateLoginInput()) return
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = authRepository.login(
                email = _email.value.trim(),
                password = _password.value
            )
            
            _authState.value = result.fold(
                onSuccess = { user ->
                    _isLoggedIn.value = true
                    _needsProfileCompletion.value = false
                    AuthState.Success(user)
                },
                onFailure = { e ->
                    AuthState.Error(e.message ?: "Login gagal")
                }
            )
        }
    }
    
    /**
     * Sign up a new user
     */
    fun signup() {
        if (!validateSignupInput()) return
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = authRepository.signup(
                email = _email.value.trim(),
                password = _password.value,
                fullName = _fullName.value.trim(),
                phoneNumber = _phoneNumber.value.trim()
            )
            
            _authState.value = result.fold(
                onSuccess = { user ->
                    _isLoggedIn.value = true
                    _needsProfileCompletion.value = false
                    AuthState.Success(user)
                },
                onFailure = { e ->
                    AuthState.Error(e.message ?: "Registrasi gagal")
                }
            )
        }
    }
    
    /**
     * Sign in with Google credential
     */
    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = authRepository.signInWithGoogle(credential)
            
            _authState.value = result.fold(
                onSuccess = { (user, needsCompletion) ->
                    _isLoggedIn.value = true
                    _needsProfileCompletion.value = needsCompletion
                    
                    // Pre-fill form with existing data
                    if (needsCompletion) {
                        _fullName.value = user.fullName
                        _phoneNumber.value = user.phoneNumber
                    }
                    
                    AuthState.Success(user, needsCompletion)
                },
                onFailure = { e ->
                    AuthState.Error(e.message ?: "Login Google gagal")
                }
            )
        }
    }
    
    /**
     * Complete profile after Google sign-in
     */
    fun completeProfile() {
        if (!validateProfileInput()) return
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = authRepository.updateUserProfile(
                fullName = _fullName.value.trim(),
                phoneNumber = _phoneNumber.value.trim()
            )
            
            _authState.value = result.fold(
                onSuccess = { user ->
                    _needsProfileCompletion.value = false
                    AuthState.Success(user)
                },
                onFailure = { e ->
                    AuthState.Error(e.message ?: "Gagal menyimpan profil")
                }
            )
        }
    }
    
    /**
     * Send password reset email
     */
    fun sendPasswordResetEmail() {
        val emailValue = _email.value.trim()
        
        if (emailValue.isEmpty()) {
            _passwordResetState.value = PasswordResetState.Error("Email harus diisi")
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            _passwordResetState.value = PasswordResetState.Error("Format email tidak valid")
            return
        }
        
        viewModelScope.launch {
            _passwordResetState.value = PasswordResetState.Loading
            
            val result = authRepository.sendPasswordResetEmail(emailValue)
            
            _passwordResetState.value = result.fold(
                onSuccess = { PasswordResetState.Success },
                onFailure = { e ->
                    PasswordResetState.Error(e.message ?: "Gagal mengirim email reset password")
                }
            )
        }
    }
    
    /**
     * Logout current user
     */
    fun logout() {
        authRepository.logout()
        _isLoggedIn.value = false
        _needsProfileCompletion.value = false
        _authState.value = AuthState.Idle
        clearForm()
    }
    
    /**
     * Reset auth state to idle
     */
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
    
    /**
     * Set Google Sign-In error message
     */
    fun setGoogleSignInError(message: String) {
        _authState.value = AuthState.Error(message)
    }
    
    /**
     * Reset password reset state to idle
     */
    fun resetPasswordResetState() {
        _passwordResetState.value = PasswordResetState.Idle
    }
    
    /**
     * Clear all form fields
     */
    fun clearForm() {
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        _fullName.value = ""
        _phoneNumber.value = ""
    }
    
    private fun validateLoginInput(): Boolean {
        val emailValue = _email.value.trim()
        val passwordValue = _password.value
        
        if (emailValue.isEmpty()) {
            _authState.value = AuthState.Error("Email harus diisi")
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            _authState.value = AuthState.Error("Format email tidak valid")
            return false
        }
        
        if (passwordValue.isEmpty()) {
            _authState.value = AuthState.Error("Password harus diisi")
            return false
        }
        
        return true
    }
    
    private fun validateSignupInput(): Boolean {
        val emailValue = _email.value.trim()
        val passwordValue = _password.value
        val confirmPasswordValue = _confirmPassword.value
        val fullNameValue = _fullName.value.trim()
        val phoneValue = _phoneNumber.value.trim()
        
        if (fullNameValue.isEmpty()) {
            _authState.value = AuthState.Error("Nama lengkap harus diisi")
            return false
        }
        
        if (emailValue.isEmpty()) {
            _authState.value = AuthState.Error("Email harus diisi")
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            _authState.value = AuthState.Error("Format email tidak valid")
            return false
        }
        
        if (phoneValue.isEmpty()) {
            _authState.value = AuthState.Error("Nomor telepon harus diisi")
            return false
        }
        
        if (passwordValue.isEmpty()) {
            _authState.value = AuthState.Error("Password harus diisi")
            return false
        }
        
        if (passwordValue.length < 6) {
            _authState.value = AuthState.Error("Password minimal 6 karakter")
            return false
        }
        
        if (confirmPasswordValue != passwordValue) {
            _authState.value = AuthState.Error("Konfirmasi password tidak cocok")
            return false
        }
        
        return true
    }
    
    private fun validateProfileInput(): Boolean {
        val fullNameValue = _fullName.value.trim()
        val phoneValue = _phoneNumber.value.trim()
        
        if (fullNameValue.isEmpty()) {
            _authState.value = AuthState.Error("Nama lengkap harus diisi")
            return false
        }
        
        if (phoneValue.isEmpty()) {
            _authState.value = AuthState.Error("Nomor telepon harus diisi")
            return false
        }
        
        return true
    }
}
