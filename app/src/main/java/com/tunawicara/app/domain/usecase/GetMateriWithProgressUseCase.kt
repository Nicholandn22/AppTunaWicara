package com.tunawicara.app.domain.usecase

import com.tunawicara.app.data.model.MateriProgress
import com.tunawicara.app.data.model.MateriWicara
import com.tunawicara.app.domain.repository.MateriWicaraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Data class combining materi with its progress status
 */
data class MateriWithProgress(
    val materi: MateriWicara,
    val sudahDirekam: Boolean = false
)

/**
 * Use case for getting materi wicara with user progress
 */
class GetMateriWithProgressUseCase @Inject constructor(
    private val materiWicaraRepository: MateriWicaraRepository
) {
    
    /**
     * Get all materi with user's progress
     * @param userId Current user's ID
     * @return Flow of list of MateriWithProgress sorted by urutan
     */
    operator fun invoke(userId: String): Flow<List<MateriWithProgress>> {
        return combine(
            materiWicaraRepository.getMateriList(),
            materiWicaraRepository.getUserProgress(userId)
        ) { materiList, progressMap ->
            materiList.map { materi ->
                MateriWithProgress(
                    materi = materi,
                    sudahDirekam = progressMap[materi.id]?.sudahDirekam ?: false
                )
            }
        }
    }
}
