package com.tunawicara.app.presentation.latihan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tunawicara.app.data.audio.AudioPlayerManager
import com.tunawicara.app.data.audio.AudioRecorderManager
import com.tunawicara.app.data.model.MateriWicara
import com.tunawicara.app.domain.repository.MateriWicaraRepository
import com.tunawicara.app.domain.usecase.GetMateriWithProgressUseCase
import com.tunawicara.app.domain.usecase.MateriWithProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatihanViewModel @Inject constructor(
    private val getMateriWithProgressUseCase: GetMateriWithProgressUseCase,
    private val materiWicaraRepository: MateriWicaraRepository,
    private val audioRecorderManager: AudioRecorderManager,
    private val audioPlayerManager: AudioPlayerManager,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow<LatihanState>(LatihanState.Loading)
    val uiState: StateFlow<LatihanState> = _uiState.asStateFlow()

    val isRecording = audioRecorderManager.isRecording
    val recordingDuration = audioRecorderManager.recordingDuration
    
    // Audio playback state
    private val _lastRecordedFilePath = MutableStateFlow<String?>(null)
    val lastRecordedFilePath: StateFlow<String?> = _lastRecordedFilePath.asStateFlow()
    
    // Similarity Score State (percentage 0-100, null if not calculated yet)
    private val _similarityScore = MutableStateFlow<Int?>(null)
    val similarityScore: StateFlow<Int?> = _similarityScore.asStateFlow()
    
    val isPlaying = audioPlayerManager.isPlaying
    val playbackPosition = audioPlayerManager.currentPosition
    val playbackDuration = audioPlayerManager.duration

    init {
        loadMateriWicara()
    }

    /**
     * Load materi wicara with user progress
     */
    fun loadMateriWicara() {
        val userId = firebaseAuth.currentUser?.uid
        
        // If user is not logged in, we might get PERMISSION_DENIED depending on rules.
        // We still attempt to fetch, using a dummy ID only if necessary for the flow logic, 
        // but awareness that Firestore might reject it is needed.
        val effectiveUserId = userId ?: "guest_user"
        
        viewModelScope.launch {
            _uiState.value = LatihanState.Loading
            
            // Try to fetch real data
            getMateriWithProgressUseCase(effectiveUserId)
                .catch { e ->
                    // Log the error for debugging
                    println("Error loading Firestore data: ${e.message}. Switching to MOCK data.")
                    
                    // Fallback to Mock Data if Firestore fails
                    val mockList = listOf(
                        MateriWithProgress(
                             MateriWicara(
                                 "1", "Laba-Laba", "kata", 1, "https://img.freepik.com/free-vector/spider-cartoon-character_1308-133065.jpg"
                             ), false
                        ),
                        MateriWithProgress(
                             MateriWicara(
                                 "2", "Kupu-Kupu", "kata", 2, "https://img.freepik.com/free-vector/butterfly-cartoon-character_1308-133066.jpg"
                             ), false
                        ),
                        MateriWithProgress(
                             MateriWicara(
                                 "3", "Bola", "kata", 3, null
                             ), true
                        )
                    )
                    
                    val currentState = _uiState.value
                    val currentIndex = if (currentState is LatihanState.Success) {
                        currentState.currentIndex
                    } else {
                        0
                    }
                    
                    _uiState.value = LatihanState.Success(
                        materiList = mockList,
                        currentIndex = currentIndex.coerceIn(0, (mockList.size - 1).coerceAtLeast(0))
                    )
                }
                .collect { materiList ->
                    if (materiList.isEmpty()) {
                        println("Firestore returned empty list. Check collection 'materi_wicara'.")
                        // Optionally could fallback to mock here too if empty is considered an error
                    }
                    
                    val currentState = _uiState.value
                    val currentIndex = if (currentState is LatihanState.Success) {
                        currentState.currentIndex
                    } else {
                        0
                    }
                    _uiState.value = LatihanState.Success(
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
        val currentState = _uiState.value
        if (currentState is LatihanState.Success && currentState.hasNext) {
            _uiState.value = currentState.copy(currentIndex = currentState.currentIndex + 1)
            // Reset score for new materi
            _similarityScore.value = null
        }
    }
    
    /**
     * Navigate to previous materi
     */
    fun previousMateri() {
        val currentState = _uiState.value
        if (currentState is LatihanState.Success && currentState.hasPrevious) {
            _uiState.value = currentState.copy(currentIndex = currentState.currentIndex - 1)
            // Reset score for new materi
            _similarityScore.value = null
        }
    }
    
    /**
     * Mark current materi as recorded and save the recording path
     */
    fun markMateriAsRecorded(recordingPath: String?) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val currentState = _uiState.value
        
        if (currentState is LatihanState.Success) {
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
                    
                    // Simulate score calculation
                    simulateSimilarityCalculation()
                }
            } else {
                // Start recording
                val filePath = audioRecorderManager.startRecording()
                filePath?.let {
                    println("Recording started: $it")
                    // Reset score when starting new recording
                    _similarityScore.value = null
                }
            }
        }
    }
    
    private fun simulateSimilarityCalculation() {
        // In a real app, this would call an AI service
        // For now, we simulate a delay and set a high score
        viewModelScope.launch {
            delay(1000) // Simulate processing time
            _similarityScore.value = 80 // Mock score as requested
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
    
    fun pausePlayback() {
        audioPlayerManager.pause()
    }
    
    fun resumePlayback() {
        audioPlayerManager.resume()
    }

    fun stopPlayback() {
        audioPlayerManager.stop()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            if (isRecording.value) {
                audioRecorderManager.stopRecording()
            }
        }
        audioPlayerManager.release()
    }
}
