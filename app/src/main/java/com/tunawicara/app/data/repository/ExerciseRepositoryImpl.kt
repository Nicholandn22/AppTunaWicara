package com.tunawicara.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.tunawicara.app.data.mapper.toDomain
import com.tunawicara.app.data.model.ExerciseEntity
import com.tunawicara.app.domain.model.Exercise
import com.tunawicara.app.domain.model.ExerciseCategory
import com.tunawicara.app.domain.repository.ExerciseRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ExerciseRepository {

    override suspend fun getExercises(): Flow<List<Exercise>> = callbackFlow {
        val listener = firestore.collection("exercises")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val exercises = snapshot.toObjects(ExerciseEntity::class.java)
                    trySend(exercises.map { it.toDomain() })
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getExercisesByCategory(category: ExerciseCategory): Flow<List<Exercise>> {
        TODO("Not yet implemented")
    }

    override suspend fun getExerciseById(id: String): Exercise? {
        TODO("Not yet implemented")
    }

    override suspend fun saveExercise(exercise: Exercise) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteExercise(id: String) {
        TODO("Not yet implemented")
    }
}
