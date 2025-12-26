package com.tunawicara.app.data.audio

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class for audio playback
 * Handles playing, pausing, and stopping audio recordings
 */
@Singleton
class AudioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()
    
    /**
     * Play audio from file path
     * @param filePath Absolute path to the audio file
     * @return true if playback started successfully
     */
    fun play(filePath: String): Boolean {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                return false
            }
            
            // Stop current playback if any
            stop()
            
            // Initialize MediaPlayer
            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                
                setOnCompletionListener {
                    _isPlaying.value = false
                    _currentPosition.value = 0
                }
                
                _duration.value = duration
                start()
                _isPlaying.value = true
            }
            
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    
    /**
     * Pause current playback
     */
    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
                _currentPosition.value = it.currentPosition
            }
        }
    }
    
    /**
     * Resume playback
     */
    fun resume() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                _isPlaying.value = true
            }
        }
    }
    
    /**
     * Stop playback and release resources
     */
    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        _isPlaying.value = false
        _currentPosition.value = 0
        _duration.value = 0
    }
    
    /**
     * Seek to position in milliseconds
     */
    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        _currentPosition.value = position
    }
    
    /**
     * Get current playback position in milliseconds
     */
    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
    
    /**
     * Clean up resources
     */
    fun release() {
        stop()
    }
}
