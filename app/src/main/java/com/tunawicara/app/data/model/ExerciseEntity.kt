package com.tunawicara.app.data.model

import com.tunawicara.app.domain.model.DifficultyLevel
import com.tunawicara.app.domain.model.ExerciseCategory

/**
 * Data model for Exercise entity
 * This represents the data structure in the data layer (database/network)
 */
data class ExerciseEntity(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val durationMinutes: Int,
    val imageUrl: String? = null
)
