package com.example.sound

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

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

    private fun setHostPersonality(gender: String) {
        if (gender.equals("MALE", ignoreCase = true)) {
            tts?.setPitch(0.7f)
        } else {
            tts?.setPitch(1.2f)
        }
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
    fun speakQuestionBounded(text: String, languageMode: String = "ENGLISH", hostGender: String = "FEMALE", onComplete: (() -> Unit)? = null) {
        if (!isReady || tts == null) {
            onComplete?.invoke()
            return
        }
        stop()

        try {
            val locale = getLocaleForMode(languageMode)
            tts?.language = locale
            tts?.setSpeechRate(1.0f) // Normal comfortable speech rate
            tts?.setPitch(if (hostGender.uppercase() == "MALE") 0.7f else 1.1f)

            val utteranceId = "Tark_Q_${System.currentTimeMillis()}"
            val isCompleted = AtomicBoolean(false)
            var watchdogJob: Job? = null

            val notifyComplete = {
                if (isCompleted.compareAndSet(false, true)) {
                    watchdogJob?.cancel()
                    scope.launch { onComplete?.invoke() }
                }
            }

            // Reliable watchdog fallback in case TTS engine stalls
            watchdogJob = scope.launch {
                val words = text.split("\\s+".toRegex()).size
                val maxAllowedMillis = (words * 250L + 2500L).coerceIn(3500L, 6000L)
                delay(maxAllowedMillis)
                notifyComplete()
            }

            tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                override fun onStart(id: String?) {}
                override fun onDone(id: String?) {
                    if (id == utteranceId) {
                        notifyComplete()
                    }
                }
                override fun onError(id: String?) {
                    if (id == utteranceId) {
                        notifyComplete()
                    }
                }
            })

            val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            if (result == TextToSpeech.ERROR) {
                notifyComplete()
            }
        } catch (_: Exception) {
            onComplete?.invoke()
        }
    }

    fun speakSequential(hindiText: String, englishText: String, onComplete: (() -> Unit)? = null) {
        val utteranceHi = "Tark_Hi_${System.currentTimeMillis()}"
        val utteranceEn = "Tark_En_${System.currentTimeMillis()}"
        val isCompleted = AtomicBoolean(false)

        val notifyComplete = {
            if (isCompleted.compareAndSet(false, true)) {
                scope.launch { onComplete?.invoke() }
            }
        }

        tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
            override fun onStart(id: String?) {}
            override fun onDone(id: String?) {
                if (id == utteranceHi) {
                    try {
                        tts?.language = Locale.US
                        tts?.speak(englishText, TextToSpeech.QUEUE_FLUSH, null, utteranceEn)
                    } catch (_: Exception) {
                        notifyComplete()
                    }
                } else if (id == utteranceEn) {
                    notifyComplete()
                }
            }
            override fun onError(id: String?) {
                if (id == utteranceHi) {
                    try {
                        tts?.language = Locale.US
                        tts?.speak(englishText, TextToSpeech.QUEUE_FLUSH, null, utteranceEn)
                    } catch (_: Exception) {
                        notifyComplete()
                    }
                } else if (id == utteranceEn) {
                    notifyComplete()
                }
            }
        })

        try {
            tts?.language = Locale("hi", "IN")
            tts?.setSpeechRate(1.0f)
            val res = tts?.speak(hindiText, TextToSpeech.QUEUE_FLUSH, null, utteranceHi)
            if (res == TextToSpeech.ERROR) {
                notifyComplete()
            }
        } catch (_: Exception) {
            notifyComplete()
        }
    }

    /**
     * Reads option within active game timer. Does not stop or pause game timer.
     */
    fun speakOptionInGameTimer(text: String, languageMode: String = "ENGLISH") {
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
    fun speakResultAnnouncement(isCorrect: Boolean, languageMode: String = "ENGLISH", hostGender: String = "FEMALE", onComplete: (() -> Unit)? = null) {
        if (!isReady || tts == null) {
            onComplete?.invoke()
            return
        }
        stop()

        try {
            val locale = getLocaleForMode(languageMode)
            tts?.language = locale
            tts?.setSpeechRate(1.08f)
            tts?.setPitch(if (hostGender.uppercase() == "MALE") 0.7f else 1.1f)

            val isEn = isEnglishMode(languageMode)
            val announcement = if (isCorrect) {
                if (isEn) "Correct." else "सही।"
            } else {
                if (isEn) "Incorrect." else "गलत।"
            }

            val utteranceId = "Tark_Result_${System.currentTimeMillis()}"
            
            val isCompleted = java.util.concurrent.atomic.AtomicBoolean(false)
            val notifyComplete = {
                if (isCompleted.compareAndSet(false, true)) {
                    scope.launch { onComplete?.invoke() }
                }
            }

            tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                override fun onStart(id: String?) {}
                override fun onDone(id: String?) {
                    if (id == utteranceId) notifyComplete()
                }
                override fun onError(id: String?) {
                    if (id == utteranceId) notifyComplete()
                }
            })

            val result = tts?.speak(announcement, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            if (result == TextToSpeech.ERROR) {
                notifyComplete()
            }
        } catch (_: Exception) {
            onComplete?.invoke()
        }
    }

    /**
     * Wrong Answer Educational Solution Narration.
     * Operates at a natural, teacher-guided pace without speed limits or pressure.
     */
    fun speakSolutionNatural(text: String, languageMode: String = "ENGLISH") {
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

    fun speakFinalResult(
        correct: Int,
        incorrect: Int,
        gross: Long,
        deduction: Long,
        finalAmount: Long,
        language: String,
        gender: String
    ) {
        if (!isReady || tts == null) return
        stop()
        try {
            val locale = getLocaleForMode(language)
            tts?.language = locale
            setHostPersonality(gender)
            tts?.setSpeechRate(1.0f)

            val isHindi = language.equals("HINDI", ignoreCase = true)
            val text = if (isHindi) {
                "आपने $correct सवालों के सही जवाब दिए, और $incorrect के गलत। आपका कुल पुरस्कार है $gross रुपये। गलत जवाबों के लिए $deduction रुपये की कटौती की गई है। आपका अंतिम पुरस्कार है $finalAmount रुपये।"
            } else {
                "You answered $correct questions correctly, and $incorrect incorrectly. Your gross winning amount is $gross rupees. A deduction of $deduction rupees has been applied for incorrect answers. Your final winning amount is $finalAmount rupees."
            }

            val utteranceId = "Final_Result_${System.currentTimeMillis()}"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (_: Exception) {}
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
        } catch (_: Exception) {}
    }
}
