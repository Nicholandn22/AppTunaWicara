package com.tunawicara.app.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tunawicara.app.data.model.User
import com.tunawicara.app.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase implementation of AuthRepository
 */
@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    
    companion object {
        private const val USERS_COLLECTION = "users"
    }
    
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user 
                ?: return Result.failure(Exception("Login gagal: user tidak ditemukan"))
            
            // Get user data from Firestore
            val userDoc = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()
            
            val user = userDoc.toObject(User::class.java)
                ?: return Result.failure(Exception("Data user tidak ditemukan"))
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }
    
    override suspend fun signup(
        email: String,
        password: String,
        fullName: String,
        phoneNumber: String
    ): Result<User> {
        return try {
            // Create auth user
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user 
                ?: return Result.failure(Exception("Registrasi gagal: user tidak dibuat"))
            
            // Create user document in Firestore
            val user = User(
                uid = firebaseUser.uid,
                email = email,
                fullName = fullName,
                phoneNumber = phoneNumber,
                role = "user",
                createdAt = Timestamp.now()
            )
            
            firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(user)
                .await()
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }
    
    override suspend fun signInWithGoogle(credential: AuthCredential): Result<Pair<User, Boolean>> {
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user 
                ?: return Result.failure(Exception("Login Google gagal"))
            
            // Try to get/create user in Firestore, but don't fail if offline
            try {
                val userDoc = firestore.collection(USERS_COLLECTION)
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                
                if (userDoc.exists()) {
                    val user = userDoc.toObject(User::class.java)
                    if (user != null) {
                        val needsProfileCompletion = user.fullName.isEmpty() || user.phoneNumber.isEmpty()
                        return Result.success(Pair(user, needsProfileCompletion))
                    }
                }
                
                // New user - create minimal profile
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    fullName = firebaseUser.displayName ?: "",
                    phoneNumber = "",
                    role = "user",
                    createdAt = Timestamp.now()
                )
                
                firestore.collection(USERS_COLLECTION)
                    .document(firebaseUser.uid)
                    .set(user)
                    .await()
                
                Result.success(Pair(user, user.phoneNumber.isEmpty()))
            } catch (firestoreError: Exception) {
                // Firestore failed but Auth succeeded - create a temporary user object
                // and let them proceed (their data will sync when online)
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    fullName = firebaseUser.displayName ?: "",
                    phoneNumber = "",
                    role = "user",
                    createdAt = Timestamp.now()
                )
                Result.success(Pair(user, false)) // Let them in, don't require profile completion
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }
    
    override suspend fun updateUserProfile(
        fullName: String,
        phoneNumber: String
    ): Result<User> {
        return try {
            val firebaseUser = firebaseAuth.currentUser 
                ?: return Result.failure(Exception("User tidak login"))
            
            val updates = mapOf(
                "fullName" to fullName,
                "phoneNumber" to phoneNumber
            )
            
            firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .update(updates)
                .await()
            
            // Get updated user
            val userDoc = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()
            
            val user = userDoc.toObject(User::class.java)
                ?: return Result.failure(Exception("Data user tidak ditemukan"))
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }
    
    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }
    
    override fun logout() {
        firebaseAuth.signOut()
    }
    
    override suspend fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        
        return try {
            val userDoc = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()
            
            userDoc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
    
    override suspend fun isProfileComplete(): Boolean {
        val user = getCurrentUser() ?: return false
        return user.fullName.isNotEmpty() && user.phoneNumber.isNotEmpty()
    }
    
    /**
     * Convert Firebase exceptions to user-friendly messages in Indonesian
     */
    private fun getErrorMessage(e: Exception): String {
        val message = e.message ?: return "Terjadi kesalahan"
        
        return when {
            message.contains("email address is badly formatted", ignoreCase = true) ->
                "Format email tidak valid"
            message.contains("password is invalid", ignoreCase = true) ||
            message.contains("wrong password", ignoreCase = true) ->
                "Password salah"
            message.contains("no user record", ignoreCase = true) ||
            message.contains("user not found", ignoreCase = true) ->
                "Email tidak terdaftar"
            message.contains("email address is already in use", ignoreCase = true) ->
                "Email sudah terdaftar"
            message.contains("password should be at least 6 characters", ignoreCase = true) ->
                "Password minimal 6 karakter"
            message.contains("network error", ignoreCase = true) ->
                "Tidak ada koneksi internet"
            message.contains("too many requests", ignoreCase = true) ->
                "Terlalu banyak percobaan. Coba lagi nanti"
            message.contains("cancelled", ignoreCase = true) ->
                "Login dibatalkan"
            else -> "Terjadi kesalahan: $message"
        }
    }
}
