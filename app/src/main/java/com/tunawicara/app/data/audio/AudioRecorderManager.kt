package com.tunawicara.app.data.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Strict AudioRecorder manager class that records audio in WAV format
 * Handles recording lifecycle and WAV file creation
 */
@Singleton
class AudioRecorderManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Audio recording parameters
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    
    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private var currentFile: File? = null
    private var startTime: Long = 0
    
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()
    
    private val _recordingDuration = MutableStateFlow(0L)
    val recordingDuration: StateFlow<Long> = _recordingDuration.asStateFlow()
    
    /**
     * Check if microphone permission is granted
     */
    fun hasRecordPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Start recording audio to WAV file
     * @return File path where recording is being saved, or null if failed
     */
    suspend fun startRecording(): String? = withContext(Dispatchers.IO) {
        if (!hasRecordPermission()) {
            return@withContext null
        }
        
        if (_isRecording.value) {
            return@withContext null
        }
        
        try {
            // Create output file
            val outputDir = File(context.getExternalFilesDir(null), "recordings")
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            
            val timestamp = System.currentTimeMillis()
            currentFile = File(outputDir, "recording_$timestamp.wav")
            
            // Initialize AudioRecord
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )
            
            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                audioRecord?.release()
                audioRecord = null
                return@withContext null
            }
            
            // Start recording
            audioRecord?.startRecording()
            _isRecording.value = true
            startTime = System.currentTimeMillis()
            
            // Start recording in background
            recordingJob = CoroutineScope(Dispatchers.IO).launch {
                writeAudioDataToFile()
            }
            
            currentFile?.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            cleanup()
            null
        }
    }
    
    /**
     * Stop recording and finalize WAV file
     * @return File path of the completed recording
     */
    suspend fun stopRecording(): String? = withContext(Dispatchers.IO) {
        if (!_isRecording.value) {
            return@withContext null
        }
        
        try {
            _isRecording.value = false
            recordingJob?.join() // Wait for recording to finish
            
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            
            // Finalize WAV file header
            currentFile?.let { file ->
                finalizeWavFile(file)
                file.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cleanup()
            null
        } finally {
            cleanup()
        }
    }
    
    /**
     * Write audio data to temporary PCM file
     */
    private suspend fun writeAudioDataToFile() {
        val tempFile = File(currentFile!!.absolutePath + ".pcm")
        val buffer = ByteArray(bufferSize)
        
        FileOutputStream(tempFile).use { fos ->
            val dos = DataOutputStream(fos)
            
            while (_isRecording.value) {
                val bytesRead = audioRecord?.read(buffer, 0, buffer.size) ?: -1
                
                if (bytesRead > 0) {
                    dos.write(buffer, 0, bytesRead)
                    
                    // Update duration
                    withContext(Dispatchers.Main) {
                        _recordingDuration.value = System.currentTimeMillis() - startTime
                    }
                }
            }
        }
        
        // Convert PCM to WAV
        convertPcmToWav(tempFile, currentFile!!)
        tempFile.delete()
    }
    
    /**
     * Convert raw PCM data to WAV file with proper headers
     */
    private fun convertPcmToWav(pcmFile: File, wavFile: File) {
        val pcmData = pcmFile.readBytes()
        val totalDataLen = pcmData.size + 36
        val channels = 1
        val byteRate = sampleRate * channels * 2
        
        FileOutputStream(wavFile).use { fos ->
            val dos = DataOutputStream(fos)
            
            // RIFF header
            dos.writeBytes("RIFF")
            dos.write(intToByteArray(totalDataLen), 0, 4)
            dos.writeBytes("WAVE")
            
            // fmt subchunk
            dos.writeBytes("fmt ")
            dos.write(intToByteArray(16), 0, 4) // Subchunk1Size (16 for PCM)
            dos.write(shortToByteArray(1), 0, 2) // AudioFormat (1 for PCM)
            dos.write(shortToByteArray(channels.toShort()), 0, 2) // NumChannels
            dos.write(intToByteArray(sampleRate), 0, 4) // SampleRate
            dos.write(intToByteArray(byteRate), 0, 4) // ByteRate
            dos.write(shortToByteArray((channels * 2).toShort()), 0, 2) // BlockAlign
            dos.write(shortToByteArray(16), 0, 2) // BitsPerSample
            
            // data subchunk
            dos.writeBytes("data")
            dos.write(intToByteArray(pcmData.size), 0, 4)
            dos.write(pcmData)
        }
    }
    
    /**
     * Finalize WAV file by updating header with correct file size
     */
    private fun finalizeWavFile(file: File) {
        val fileSize = file.length()
        
        RandomAccessFile(file, "rw").use { raf ->
            // Update RIFF chunk size
            raf.seek(4)
            raf.write(intToByteArray((fileSize - 8).toInt()), 0, 4)
            
            // Update data chunk size
            raf.seek(40)
            raf.write(intToByteArray((fileSize - 44).toInt()), 0, 4)
        }
    }
    
    /**
     * Convert int to little-endian byte array
     */
    private fun intToByteArray(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xff).toByte(),
            ((value shr 8) and 0xff).toByte(),
            ((value shr 16) and 0xff).toByte(),
            ((value shr 24) and 0xff).toByte()
        )
    }
    
    /**
     * Convert short to little-endian byte array
     */
    private fun shortToByteArray(value: Short): ByteArray {
        return byteArrayOf(
            (value.toInt() and 0xff).toByte(),
            ((value.toInt() shr 8) and 0xff).toByte()
        )
    }
    
    /**
     * Clean up resources
     */
    private fun cleanup() {
        audioRecord?.release()
        audioRecord = null
        recordingJob = null
        currentFile = null
        _recordingDuration.value = 0
    }
    
    /**
     * Cancel recording and delete the file
     */
    suspend fun cancelRecording() = withContext(Dispatchers.IO) {
        stopRecording()
        currentFile?.delete()
        currentFile = null
    }
}
