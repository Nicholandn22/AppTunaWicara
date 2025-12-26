package com.tunawicara.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunawicara.app.data.audio.AudioRecorderManager
import com.tunawicara.app.data.audio.AudioPlayerManager
import com.tunawicara.app.domain.usecase.GetExercisesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Home Screen
 * Manages UI state, business logic, and audio recording
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val audioRecorderManager: AudioRecorderManager,
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {
    
    
    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    val isRecording = audioRecorderManager.isRecording
    val recordingDuration = audioRecorderManager.recordingDuration
    
    // Audio playback state
    private val _lastRecordedFilePath = MutableStateFlow<String?>(null)
    val lastRecordedFilePath: StateFlow<String?> = _lastRecordedFilePath.asStateFlow()
    
    val isPlaying = audioPlayerManager.isPlaying
    val playbackPosition = audioPlayerManager.currentPosition
    val playbackDuration = audioPlayerManager.duration

    
    init {
        loadExercises()
    }
    
    fun loadExercises() {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            getExercisesUseCase()
                .catch { e ->
                    _state.value = HomeState.Error(
                        e.message ?: "Terjadi kesalahan saat memuat data"
                    )
                }
                .collect { exercises ->
                    _state.value = HomeState.Success(exercises)
                }
        }
    }
    
    /**
     * Toggle recording with permission check
     */
    fun toggleRecordingWithPermission(onPermissionNeeded: () -> Unit) {
        if (!audioRecorderManager.hasRecordPermission()) {
            onPermissionNeeded()
            return
        }
        
        toggleRecording()
    }
    
    /**
     * Toggle recording state (start/stop)
     */
    fun toggleRecording() {
        viewModelScope.launch {
            if (isRecording.value) {
                // Stop recording
                val filePath = audioRecorderManager.stopRecording()
                filePath?.let {
                    _lastRecordedFilePath.value = it
                    println("Recording saved to: $it")
                }
            } else {
                // Start recording
                val filePath = audioRecorderManager.startRecording()
                filePath?.let {
                    println("Recording started: $it")
                }
            }
        }
    }
    
    /**
     * Play the last recorded audio
     */
    fun playRecording() {
        _lastRecordedFilePath.value?.let { filePath ->
            audioPlayerManager.play(filePath)
        }
    }
    
    /**
     * Pause playback
     */
    fun pausePlayback() {
        audioPlayerManager.pause()
    }
    
    /**
     * Resume playback
     */
    fun resumePlayback() {
        audioPlayerManager.resume()
    }
    
    /**
     * Stop playback
     */
    fun stopPlayback() {
        audioPlayerManager.stop()
    }

    
    
    override fun onCleared() {
        super.onCleared()
        // Ensure recording is stopped when ViewModel is cleared
        viewModelScope.launch {
            if (isRecording.value) {
                audioRecorderManager.stopRecording()
            }
        }
        // Stop playback
        audioPlayerManager.release()
    }
}
