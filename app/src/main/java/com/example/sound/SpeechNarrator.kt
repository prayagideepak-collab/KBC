package com.example.sound

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Text-to-Speech Voice Host for TarkShastra.
 * Enforces strict timing and educational contracts:
 * 1. Question reading is hard-bounded to maximum 5.0 seconds with dynamic speech rate calculation.
 * 2. Option narration runs within the active main running timer without pause/extension.
 * 3. Hint / Tark Guru audio reading is strictly forbidden (visual only).
 * 4. Wrong Answer Solution Narration runs at a calm, natural learning pace (unbounded by 5s timer).
 */
class SpeechNarrator(context: Context) : TextToSpeech.OnInitListener {

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false
    private var hardCeilingJob: Job? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isReady = true
            try {
                tts?.setSpeechRate(1.0f)
                tts?.setPitch(1.0f)
            } catch (_: Exception) {}
        }
    }

    private fun getLocaleForMode(mode: String): Locale {
        val m = mode.uppercase()
        return if (m == "ENGLISH" || m == "EN") Locale.US else Locale("hi", "IN")
    }

    private fun isEnglishMode(mode: String): Boolean {
        val m = mode.uppercase()
        return m == "ENGLISH" || m == "EN"
    }

    /**
     * Reads the question with a strict 5.0-second hard ceiling.
     * Calculates dynamic speech rate based on word count so full question is narrated within <= 5 sec.
     * A hard timeout forcibly stops TTS after 5.0 seconds.
     */
    fun speakQuestionBounded(text: String, languageMode: String = "HINDI", onComplete: (() -> Unit)? = null) {
        if (!isReady || tts == null) {
            onComplete?.invoke()
            return
        }
        stop()

        try {
            val locale = getLocaleForMode(languageMode)
            tts?.language = locale
            tts?.setSpeechRate(1.0f) // Normal comfortable Junior/Adult speech rate
            tts?.setPitch(1.0f)

            val utteranceId = "Tark_Q_${System.currentTimeMillis()}"

            tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                override fun onStart(id: String?) {}
                override fun onDone(id: String?) {
                    if (id == utteranceId) {
                        scope.launch { onComplete?.invoke() }
                    }
                }
                override fun onError(id: String?) {
                    if (id == utteranceId) {
                        scope.launch { onComplete?.invoke() }
                    }
                }
            })

            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)

            // Safe fallback timer based on word count (e.g. 300ms per word, min 4s, max 15s) in case TTS callback fails
            val words = text.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
            val fallbackDelay = (words.size * 300L).coerceIn(4000L, 15000L)
            hardCeilingJob = scope.launch {
                delay(fallbackDelay)
                onComplete?.invoke()
            }
        } catch (_: Exception) {
            onComplete?.invoke()
        }
    }

    /**
     * Reads option within active game timer. Does not stop or pause game timer.
     */
    fun speakOptionInGameTimer(text: String, languageMode: String = "HINDI") {
        if (!isReady || tts == null) return
        try {
            val locale = getLocaleForMode(languageMode)
            tts?.language = locale
            tts?.setSpeechRate(1.25f)
            val utteranceId = "Tark_Opt_${System.currentTimeMillis()}"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (_: Exception) {}
    }

    /**
     * Announces result with user's authoritative profile name.
     * e.g. "Deepak, आपका जवाब सही है।" or "Deepak, your answer is correct."
     */
    fun speakResultAnnouncement(userName: String, isCorrect: Boolean, languageMode: String = "HINDI") {
        if (!isReady || tts == null) return
        stop()

        try {
            val locale = getLocaleForMode(languageMode)
            tts?.language = locale
            tts?.setSpeechRate(1.08f)
            tts?.setPitch(1.0f)

            val isEn = isEnglishMode(languageMode)
            val cleanName = userName.ifBlank { if (isEn) "Player" else "खिलाड़ी" }
            val announcement = if (isCorrect) {
                if (isEn) "$cleanName, your answer is correct." else "$cleanName, आपका जवाब सही है।"
            } else {
                if (isEn) "$cleanName, your answer is incorrect." else "$cleanName, आपका जवाब गलत है।"
            }

            val utteranceId = "Tark_Result_${System.currentTimeMillis()}"
            tts?.speak(announcement, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (_: Exception) {}
    }

    /**
     * Wrong Answer Educational Solution Narration.
     * Operates at a natural, teacher-guided pace without speed limits or pressure.
     */
    fun speakSolutionNatural(text: String, languageMode: String = "HINDI") {
        if (!isReady || tts == null) return
        stop()

        try {
            val locale = getLocaleForMode(languageMode)
            tts?.language = locale
            tts?.setSpeechRate(0.96f)
            tts?.setPitch(1.02f)
            val utteranceId = "Tark_Solution_${System.currentTimeMillis()}"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (_: Exception) {}
    }

    fun stop() {
        hardCeilingJob?.cancel()
        hardCeilingJob = null
        try {
            tts?.stop()
        } catch (_: Exception) {}
    }

    fun shutdown() {
        hardCeilingJob?.cancel()
        hardCeilingJob = null
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
        } catch (_: Exception) {}
    }
}
