package com.tunawicara.app.presentation.latihan

import com.tunawicara.app.domain.usecase.MateriWithProgress

/**
 * UI State for Latihan Screen
 */
sealed class LatihanState {
    object Loading : LatihanState()
    data class Success(
        val materiList: List<MateriWithProgress>,
        val currentIndex: Int = 0
    ) : LatihanState() {
        val currentMateri: MateriWithProgress?
            get() = materiList.getOrNull(currentIndex)
        
        val hasNext: Boolean
            get() = currentIndex < materiList.size - 1
        
        val hasPrevious: Boolean
            get() = currentIndex > 0
    }
    data class Error(val message: String) : LatihanState()
}
