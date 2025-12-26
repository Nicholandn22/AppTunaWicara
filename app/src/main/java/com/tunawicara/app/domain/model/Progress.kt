package com.tunawicara.app.domain.model

import java.time.LocalDate

/**
 * Domain model representing user progress in speech therapy
 */
data class Progress(
    val userId: String,
    val exerciseId: String,
    val completedDate: LocalDate,
    val score: Int,
    val durationSeconds: Int,
    val notes: String? = null
)

/**
 * Summary of user's overall progress
 */
data class ProgressSummary(
    val totalExercisesCompleted: Int,
    val averageScore: Double,
    val totalTimeSpentMinutes: Int,
    val currentStreak: Int,
    val lastCompletedDate: LocalDate?
)
