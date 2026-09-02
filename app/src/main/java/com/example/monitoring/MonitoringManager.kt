package com.example.monitoring

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

class MonitoringManager(private val context: Context, private val scope: CoroutineScope) {

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null

    private val _waveform = MutableStateFlow<List<Float>>(List(6) { 0.1f })
    val waveform: StateFlow<List<Float>> = _waveform.asStateFlow()

    private val _audioState = MutableStateFlow("NORMAL")
    val audioState: StateFlow<String> = _audioState.asStateFlow()

    private val _warningEvent = MutableStateFlow<Long>(0L)
    val warningEvent: StateFlow<Long> = _warningEvent.asStateFlow()

    private val sampleRate = 16000
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    ) * 2

    // States for calculating warnings
    private var highActivityDurationMs = 0L
    private var lastEventTime = 0L
    private val CONFIRMATION_DURATION_MS = 2000L // 2 seconds of sustained high activity
    private val COOLDOWN_DURATION_MS = 5000L // 5 seconds cooldown after a warning

    fun startMonitoring() {
        if (recordingJob?.isActive == true) return
        
        val hasMicPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        if (!hasMicPermission) {
            return
        }

        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            audioRecord?.startRecording()
            
            recordingJob = scope.launch(Dispatchers.IO) {
                val buffer = ShortArray(bufferSize / 2)
                var lastProcessTime = System.currentTimeMillis()
                
                while (isActive) {
                    val readResult = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (readResult > 0) {
                        // Calculate RMS (Root Mean Square)
                        var sum = 0.0
                        for (i in 0 until readResult) {
                            sum += buffer[i] * buffer[i]
                        }
                        val rms = Math.sqrt(sum / readResult).toFloat()
                        
                        // Normalize 0.0 to 1.0 (roughly, 32767 is max for 16-bit)
                        val normalized = (rms / 32767f) * 10f // amplify it a bit for visual clarity
                        val clamped = normalized.coerceIn(0.1f, 1.0f)
                        
                        val currentTime = System.currentTimeMillis()
                        val dt = currentTime - lastProcessTime
                        
                        if (dt >= 100) { // Update ~10 times per second
                            lastProcessTime = currentTime
                            updateWaveform(clamped)
                            
                            val newState = if (clamped > 0.8f) "HIGH" else if (clamped > 0.4f) "ELEVATED" else "NORMAL"
                            _audioState.value = newState
                            
                            checkWarnings(newState, dt)
                        }
                    }
                    delay(10) // slight delay to prevent 100% CPU on fast read
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Graceful fallback, no crash
        }
    }

    private fun updateWaveform(newValue: Float) {
        val currentList = _waveform.value.toMutableList()
        currentList.removeAt(0)
        currentList.add(newValue)
        _waveform.value = currentList
    }

    private fun checkWarnings(state: String, dt: Long) {
        val currentTime = System.currentTimeMillis()
        
        // Cooldown period
        if (currentTime - lastEventTime < COOLDOWN_DURATION_MS) {
            highActivityDurationMs = 0
            return
        }
        
        if (state == "HIGH") {
            highActivityDurationMs += dt
            if (highActivityDurationMs >= CONFIRMATION_DURATION_MS) {
                // Confirmed violation
                _warningEvent.value = currentTime
                lastEventTime = currentTime
                highActivityDurationMs = 0
            }
        } else {
            // Decay or reset high activity duration
            highActivityDurationMs = maxOf(0L, highActivityDurationMs - dt * 2)
        }
    }

    fun stopMonitoring() {
        recordingJob?.cancel()
        recordingJob = null
        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (e: Exception) {}
        audioRecord = null
        
        _waveform.value = List(6) { 0.1f }
        _audioState.value = "NORMAL"
        highActivityDurationMs = 0L
    }
}
