package com.tunawicara.app.domain.model

/**
 * Domain model representing an audio recording
 */
data class AudioRecording(
    val id: String,
    val filePath: String,
    val fileName: String,
    val durationMillis: Long,
    val timestamp: Long,
    val exerciseId: String? = null
)

/**
 * Sealed class representing recording states
 */
sealed class RecordingState {
    object Idle : RecordingState()
    object Recording : RecordingState()
    object Paused : RecordingState()
    data class Error(val message: String) : RecordingState()
}
