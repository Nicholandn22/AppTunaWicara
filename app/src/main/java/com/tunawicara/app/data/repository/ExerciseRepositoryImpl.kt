package com.tunawicara.app.data.repository

import com.tunawicara.app.data.mapper.toDomain
import com.tunawicara.app.data.model.ExerciseEntity
import com.tunawicara.app.domain.model.Exercise
import com.tunawicara.app.domain.model.ExerciseCategory
import com.tunawicara.app.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ExerciseRepository
 * Currently uses in-memory data, can be extended to use Room database or remote API
 */
@Singleton
class ExerciseRepositoryImpl @Inject constructor() : ExerciseRepository {
    
    // Sample data for demonstration
    private val sampleExercises = mutableListOf(
        ExerciseEntity(
            id = "1",
            title = "Latihan Pelafalan Huruf A",
            description = "Latihan mengucapkan huruf 'A' dengan jelas dan tepat. Ulangi sebanyak 10 kali dengan perlahan.",
            category = ExerciseCategory.ARTICULATION.name,
            difficulty = "BEGINNER",
            durationMinutes = 5
        ),
        ExerciseEntity(
            id = "2",
            title = "Latihan Pernapasan Diafragma",
            description = "Latihan bernapas menggunakan diafragma untuk meningkatkan kontrol suara.",
            category = ExerciseCategory.BREATHING.name,
            difficulty = "BEGINNER",
            durationMinutes = 10
        ),
        ExerciseEntity(
            id = "3",
            title = "Latihan Kata Sederhana",
            description = "Praktik mengucapkan kata-kata sederhana seperti 'mama', 'papa', 'air', 'makan'.",
            category = ExerciseCategory.VOCABULARY.name,
            difficulty = "BEGINNER",
            durationMinutes = 15
        ),
        ExerciseEntity(
            id = "4",
            title = "Resonansi Suara",
            description = "Latihan untuk meningkatkan resonansi dan kualitas suara dengan teknik humming.",
            category = ExerciseCategory.RESONANCE.name,
            difficulty = "INTERMEDIATE",
            durationMinutes = 8
        ),
        ExerciseEntity(
            id = "5",
            title = "Latihan Membaca Kalimat",
            description = "Membaca kalimat panjang dengan intonasi yang tepat dan jelas.",
            category = ExerciseCategory.ARTICULATION.name,
            difficulty = "ADVANCED",
            durationMinutes = 20
        )
    )
    
    override suspend fun getExercises(): Flow<List<Exercise>> = flow {
        emit(sampleExercises.map { it.toDomain() })
    }
    
    override suspend fun getExercisesByCategory(category: ExerciseCategory): Flow<List<Exercise>> = flow {
        val filtered = sampleExercises
            .filter { it.category == category.name }
            .map { it.toDomain() }
        emit(filtered)
    }
    
    override suspend fun getExerciseById(id: String): Exercise? {
        return sampleExercises.find { it.id == id }?.toDomain()
    }
    
    override suspend fun saveExercise(exercise: Exercise) {
        // In a real app, this would save to database
        // For now, just add to the in-memory list
        val entity = ExerciseEntity(
            id = exercise.id,
            title = exercise.title,
            description = exercise.description,
            category = exercise.category.name,
            difficulty = exercise.difficulty.name,
            durationMinutes = exercise.durationMinutes,
            imageUrl = exercise.imageUrl
        )
        
        val existingIndex = sampleExercises.indexOfFirst { it.id == exercise.id }
        if (existingIndex >= 0) {
            sampleExercises[existingIndex] = entity
        } else {
            sampleExercises.add(entity)
        }
    }
    
    override suspend fun deleteExercise(id: String) {
        sampleExercises.removeIf { it.id == id }
    }
}
