package com.tunawicara.app.domain.repository

import com.google.firebase.auth.AuthCredential
import com.tunawicara.app.data.model.User

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {
    
    /**
     * Login with email and password
     */
    suspend fun login(email: String, password: String): Result<User>
    
    /**
     * Sign up a new user
     */
    suspend fun signup(
        email: String,
        password: String,
        fullName: String,
        phoneNumber: String
    ): Result<User>
    
    /**
     * Sign in with Google credential
     * Returns Pair<User, Boolean> where Boolean is true if user needs to complete profile
     */
    suspend fun signInWithGoogle(credential: AuthCredential): Result<Pair<User, Boolean>>
    
    /**
     * Update user profile (for completing profile after Google sign-in)
     */
    suspend fun updateUserProfile(
        fullName: String,
        phoneNumber: String
    ): Result<User>
    
    /**
     * Send password reset email
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    
    /**
     * Logout current user
     */
    fun logout()
    
    /**
     * Get currently logged in user
     */
    suspend fun getCurrentUser(): User?
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean
    
    /**
     * Check if user profile is complete
     */
    suspend fun isProfileComplete(): Boolean
}
