package com.tunawicara.app.data.model

import com.google.firebase.Timestamp

/**
 * User data class for Firebase Firestore
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val role: String = "user",
    val createdAt: Timestamp? = null
)
