package com.tunawicara.app.domain.repository

import com.tunawicara.app.domain.model.Exercise
import com.tunawicara.app.domain.model.ExerciseCategory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for exercise data operations
 * Following the Repository pattern from Clean Architecture
 */
interface ExerciseRepository {
    
    /**
     * Get all available exercises
     */
    suspend fun getExercises(): Flow<List<Exercise>>
    
    /**
     * Get exercises filtered by category
     */
    suspend fun getExercisesByCategory(category: ExerciseCategory): Flow<List<Exercise>>
    
    /**
     * Get a specific exercise by ID
     */
    suspend fun getExerciseById(id: String): Exercise?
    
    /**
     * Save or update an exercise
     */
    suspend fun saveExercise(exercise: Exercise)
    
    /**
     * Delete an exercise
     */
    suspend fun deleteExercise(id: String)
}
