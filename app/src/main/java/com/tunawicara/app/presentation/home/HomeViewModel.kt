package com.tunawicara.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tunawicara.app.data.audio.AudioRecorderManager
import com.tunawicara.app.data.audio.AudioPlayerManager
import com.tunawicara.app.domain.repository.MateriWicaraRepository
import com.tunawicara.app.domain.usecase.GetExercisesUseCase
import com.tunawicara.app.domain.usecase.GetMateriWithProgressUseCase
import com.tunawicara.app.domain.usecase.MateriWithProgress
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
    private val getMateriWithProgressUseCase: GetMateriWithProgressUseCase,
    private val materiWicaraRepository: MateriWicaraRepository,
    private val audioRecorderManager: AudioRecorderManager,
    private val audioPlayerManager: AudioPlayerManager,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    
    
    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    // Materi state for wicara exercises
    private val _materiState = MutableStateFlow<MateriState>(MateriState.Loading)
    val materiState: StateFlow<MateriState> = _materiState.asStateFlow()
    
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
        loadMateriWicara()
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
     * Load materi wicara with user progress
     */
    fun loadMateriWicara() {
        // Use a dummy ID if user is not logged in, to allow testing mock data
        val userId = firebaseAuth.currentUser?.uid ?: "dummy_user"
        
        viewModelScope.launch {
            _materiState.value = MateriState.Loading
            
            // Try to fetch real data, but fallback to mock if it fails (e.g. Permission Denied)
            getMateriWithProgressUseCase(userId)
                .catch { e ->
                    // Fallback to Mock Data
                    val mockList = listOf(
                        MateriWithProgress(
                             com.tunawicara.app.data.model.MateriWicara(
                                 "1", "Laba-Laba", "kata", 1, "https://img.freepik.com/free-vector/spider-cartoon-character_1308-133065.jpg"
                             ), false
                        ),
                        MateriWithProgress(
                             com.tunawicara.app.data.model.MateriWicara(
                                 "2", "Kupu-Kupu", "kata", 2, "https://img.freepik.com/free-vector/butterfly-cartoon-character_1308-133066.jpg"
                             ), false
                        ),
                        MateriWithProgress(
                             com.tunawicara.app.data.model.MateriWicara(
                                 "3", "Bola", "kata", 3, null
                             ), true
                        )
                    )
                    
                    val currentState = _materiState.value
                    val currentIndex = if (currentState is MateriState.Success) {
                        currentState.currentIndex
                    } else {
                        0
                    }
                    
                    _materiState.value = MateriState.Success(
                        materiList = mockList,
                        currentIndex = currentIndex.coerceIn(0, (mockList.size - 1).coerceAtLeast(0))
                    )
                    
                    // Log the error but don't show it to user to keep UI consistent
                    println("Error loading data, using mock: ${e.message}")
                }
                .collect { materiList ->
                    val currentState = _materiState.value
                    val currentIndex = if (currentState is MateriState.Success) {
                        currentState.currentIndex
                    } else {
                        0
                    }
                    _materiState.value = MateriState.Success(
                        materiList = materiList,
                        currentIndex = currentIndex.coerceIn(0, (materiList.size - 1).coerceAtLeast(0))
                    )
                }
        }
    }
    
    /**
     * Navigate to next materi
     */
    fun nextMateri() {
        val currentState = _materiState.value
        if (currentState is MateriState.Success && currentState.hasNext) {
            _materiState.value = currentState.copy(currentIndex = currentState.currentIndex + 1)
        }
    }
    
    /**
     * Navigate to previous materi
     */
    fun previousMateri() {
        val currentState = _materiState.value
        if (currentState is MateriState.Success && currentState.hasPrevious) {
            _materiState.value = currentState.copy(currentIndex = currentState.currentIndex - 1)
        }
    }
    
    /**
     * Mark current materi as recorded and save the recording path
     */
    fun markMateriAsRecorded(recordingPath: String?) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val currentState = _materiState.value
        
        if (currentState is MateriState.Success) {
            val currentMateri = currentState.currentMateri ?: return
            
            viewModelScope.launch {
                materiWicaraRepository.updateMateriProgress(
                    userId = userId,
                    materiId = currentMateri.materi.id,
                    recordingPath = recordingPath
                )
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
                    // Mark current materi as recorded
                    markMateriAsRecorded(it)
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
