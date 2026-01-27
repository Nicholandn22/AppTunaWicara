package com.tunawicara.app.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tunawicara.app.data.model.MateriProgress
import com.tunawicara.app.data.model.MateriWicara
import com.tunawicara.app.domain.repository.MateriWicaraRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase implementation of MateriWicaraRepository
 */
@Singleton
class MateriWicaraRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MateriWicaraRepository {
    
    companion object {
        private const val MATERI_COLLECTION = "materi_wicara"
        private const val USERS_COLLECTION = "users"
        private const val PROGRESS_SUBCOLLECTION = "materi_progress"
    }
    
    override fun getMateriList(): Flow<List<MateriWicara>> = callbackFlow {
        val listenerRegistration = firestore.collection(MATERI_COLLECTION)
            .orderBy("urutan", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val materiList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(MateriWicara::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(materiList)
            }
        
        awaitClose { listenerRegistration.remove() }
    }
    
    override suspend fun getMateriById(id: String): MateriWicara? {
        return try {
            val doc = firestore.collection(MATERI_COLLECTION)
                .document(id)
                .get()
                .await()
            doc.toObject(MateriWicara::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }
    
    override fun getUserProgress(userId: String): Flow<Map<String, MateriProgress>> = callbackFlow {
        val listenerRegistration = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(PROGRESS_SUBCOLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Don't close, just emit empty - progress might not exist yet
                    trySend(emptyMap())
                    return@addSnapshotListener
                }
                
                val progressMap = snapshot?.documents?.mapNotNull { doc ->
                    val progress = doc.toObject(MateriProgress::class.java)
                    progress?.let { doc.id to it }
                }?.toMap() ?: emptyMap()
                
                trySend(progressMap)
            }
        
        awaitClose { listenerRegistration.remove() }
    }
    
    override suspend fun updateMateriProgress(
        userId: String,
        materiId: String,
        recordingPath: String?
    ): Result<Unit> {
        return try {
            val progress = MateriProgress(
                materiId = materiId,
                sudahDirekam = true,
                recordedAt = Timestamp.now(),
                recordingPath = recordingPath
            )
            
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(PROGRESS_SUBCOLLECTION)
                .document(materiId)
                .set(progress)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
