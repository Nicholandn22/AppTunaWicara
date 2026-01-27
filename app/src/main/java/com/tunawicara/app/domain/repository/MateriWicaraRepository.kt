package com.tunawicara.app.domain.repository

import com.tunawicara.app.data.model.MateriProgress
import com.tunawicara.app.data.model.MateriWicara
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for materi wicara operations
 */
interface MateriWicaraRepository {
    
    /**
     * Get all materi sorted by urutan (1-70)
     */
    fun getMateriList(): Flow<List<MateriWicara>>
    
    /**
     * Get materi by its document ID
     */
    suspend fun getMateriById(id: String): MateriWicara?
    
    /**
     * Get user's progress for all materi
     */
    fun getUserProgress(userId: String): Flow<Map<String, MateriProgress>>
    
    /**
     * Update user's recording status for a materi
     */
    suspend fun updateMateriProgress(
        userId: String, 
        materiId: String, 
        recordingPath: String?
    ): Result<Unit>
}
