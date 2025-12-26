package com.tunawicara.app.presentation.home

import com.tunawicara.app.domain.model.Exercise

/**
 * UI State for Home Screen
 */
sealed class HomeState {
    object Loading : HomeState()
    data class Success(val exercises: List<Exercise>) : HomeState()
    data class Error(val message: String) : HomeState()
}
