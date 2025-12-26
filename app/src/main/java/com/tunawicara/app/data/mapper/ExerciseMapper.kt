package com.tunawicara.app.data.mapper

import com.tunawicara.app.data.model.ExerciseEntity
import com.tunawicara.app.domain.model.DifficultyLevel
import com.tunawicara.app.domain.model.Exercise
import com.tunawicara.app.domain.model.ExerciseCategory

/**
 * Mapper functions to convert between data and domain models
 */
fun ExerciseEntity.toDomain(): Exercise {
    return Exercise(
        id = id,
        title = title,
        description = description,
        category = ExerciseCategory.valueOf(category),
        difficulty = DifficultyLevel.valueOf(difficulty),
        durationMinutes = durationMinutes,
        imageUrl = imageUrl
    )
}

fun Exercise.toEntity(): ExerciseEntity {
    return ExerciseEntity(
        id = id,
        title = title,
        description = description,
        category = category.name,
        difficulty = difficulty.name,
        durationMinutes = durationMinutes,
        imageUrl = imageUrl
    )
}
