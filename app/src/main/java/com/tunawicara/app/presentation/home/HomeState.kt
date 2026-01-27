package com.tunawicara.app.presentation.home

import com.tunawicara.app.domain.model.Exercise
import com.tunawicara.app.domain.usecase.MateriWithProgress

/**
 * UI State for Home Screen
 */
sealed class HomeState {
    object Loading : HomeState()
    data class Success(val exercises: List<Exercise>) : HomeState()
    data class Error(val message: String) : HomeState()
}

/**
 * UI State for Materi Wicara display
 */
sealed class MateriState {
    object Loading : MateriState()
    data class Success(
        val materiList: List<MateriWithProgress>,
        val currentIndex: Int = 0
    ) : MateriState() {
        val currentMateri: MateriWithProgress?
            get() = materiList.getOrNull(currentIndex)
        
        val hasNext: Boolean
            get() = currentIndex < materiList.size - 1
        
        val hasPrevious: Boolean
            get() = currentIndex > 0
    }
    data class Error(val message: String) : MateriState()
}
