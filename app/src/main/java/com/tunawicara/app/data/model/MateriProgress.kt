package com.tunawicara.app.data.model

import com.google.firebase.Timestamp

/**
 * Data model for user progress on materi_wicara
 * Stored in: users/{userId}/materi_progress/{materiId}
 */
data class MateriProgress(
    val materiId: String = "",
    val sudahDirekam: Boolean = false,
    val recordedAt: Timestamp? = null,
    val recordingPath: String? = null
) {
    // No-arg constructor for Firestore
    constructor() : this("", false, null, null)
}
