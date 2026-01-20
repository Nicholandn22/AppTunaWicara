package com.tunawicara.app.presentation.auth

import com.tunawicara.app.data.model.User

/**
 * Sealed class representing authentication UI states
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User? = null, val needsProfileCompletion: Boolean = false) : AuthState()
    data class Error(val message: String) : AuthState()
}

/**
 * Sealed class for password reset UI states
 */
sealed class PasswordResetState {
    object Idle : PasswordResetState()
    object Loading : PasswordResetState()
    object Success : PasswordResetState()
    data class Error(val message: String) : PasswordResetState()
}
