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

    /**
     * Reads the question with a strict 5.0-second hard ceiling.
     * Calculates dynamic speech rate based on word count so full question is narrated within <= 5 sec.
     * A hard timeout forcibly stops TTS after 5.0 seconds.
     */
    fun speakQuestionBounded(text: String, lang: String = "hi", maxDurationSec: Float = 5.0f) {
        if (!isReady || tts == null) return
        stop()

        try {
            val locale = if (lang == "hi") Locale("hi", "IN") else Locale.US
            tts?.language = locale

            // Calculate dynamic speech rate
            // Standard reading speed is ~2.6 words per second (13 words in 5 sec).
            val words = text.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
            val wordCount = words.size.coerceAtLeast(1)
            val baseDurationAt1x = wordCount / 2.6f

            val dynamicRate = if (baseDurationAt1x > maxDurationSec) {
                (baseDurationAt1x / maxDurationSec * 1.08f).coerceIn(1.0f, 3.5f)
            } else {
                1.0f
            }

            tts?.setSpeechRate(dynamicRate)
            val utteranceId = "Tark_Q_${System.currentTimeMillis()}"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)

            // Hard ceiling cancellation timer at maxDurationSec (5 seconds)
            hardCeilingJob = scope.launch {
                delay((maxDurationSec * 1000).toLong())
                stop()
            }
        } catch (_: Exception) {}
    }

    /**
     * Reads option within active game timer. Does not stop or pause game timer.
     */
    fun speakOptionInGameTimer(text: String, lang: String = "hi") {
        if (!isReady || tts == null) return
        try {
            val locale = if (lang == "hi") Locale("hi", "IN") else Locale.US
            tts?.language = locale
            tts?.setSpeechRate(1.25f)
            val utteranceId = "Tark_Opt_${System.currentTimeMillis()}"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (_: Exception) {}
    }

    /**
     * Announces result with user's authoritative profile name.
     * e.g. "Deepak, आपका जवाब सही है।" or "Deepak, आपका जवाब गलत है।"
     */
    fun speakResultAnnouncement(userName: String, isCorrect: Boolean, lang: String = "hi") {
        if (!isReady || tts == null) return
        stop()

        try {
            val locale = if (lang == "hi") Locale("hi", "IN") else Locale.US
            tts?.language = locale
            tts?.setSpeechRate(1.08f)
            tts?.setPitch(1.0f)

            val cleanName = userName.ifBlank { if (lang == "hi") "खिलाड़ी" else "Player" }
            val announcement = if (isCorrect) {
                if (lang == "hi") "$cleanName, आपका जवाब सही है।" else "$cleanName, your answer is correct."
            } else {
                if (lang == "hi") "$cleanName, आपका जवाब गलत है।" else "$cleanName, your answer is incorrect."
            }

            val utteranceId = "Tark_Result_${System.currentTimeMillis()}"
            tts?.speak(announcement, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (_: Exception) {}
    }

    /**
     * Wrong Answer Educational Solution Narration.
     * Operates at a natural, teacher-guided pace without speed limits or pressure.
     */
    fun speakSolutionNatural(text: String, lang: String = "hi") {
        if (!isReady || tts == null) return
        stop()

        try {
            val locale = if (lang == "hi") Locale("hi", "IN") else Locale.US
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
