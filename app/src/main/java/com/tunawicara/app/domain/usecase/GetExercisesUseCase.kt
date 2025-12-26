package com.tunawicara.app.domain.usecase

import com.tunawicara.app.domain.model.Exercise
import com.tunawicara.app.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all exercises
 * Encapsulates business logic for getting exercises
 */
class GetExercisesUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(): Flow<List<Exercise>> {
        return exerciseRepository.getExercises()
    }
}
