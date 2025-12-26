package com.tunawicara.app.domain.model

/**
 * Domain model representing a speech therapy exercise
 */
data class Exercise(
    val id: String,
    val title: String,
    val description: String,
    val category: ExerciseCategory,
    val difficulty: DifficultyLevel,
    val durationMinutes: Int,
    val imageUrl: String? = null
)

enum class ExerciseCategory {
    ARTICULATION,      // Latihan artikulasi
    PHONATION,         // Latihan fonasi
    RESONANCE,         // Latihan resonansi
    BREATHING,         // Latihan pernapasan
    VOCABULARY         // Latihan kosakata
}

enum class DifficultyLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}
