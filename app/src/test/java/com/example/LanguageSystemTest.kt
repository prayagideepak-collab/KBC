package com.example

import com.example.data.model.QuestionItem
import com.example.data.model.UserProfile
import com.example.util.LanguageResolver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LanguageSystemTest {

    private val sampleQuestion = QuestionItem(
        id = "test_q1",
        qNumber = 1,
        difficultyTitle = "Easy",
        timeLimitSeconds = 60,
        points = 1000L,
        isCheckpoint = false,
        category = "Logic",
        questionHindi = "भारत की राजधानी क्या है?",
        questionEnglish = "What is the capital of India?",
        cluesHindi = listOf("यह एक ऐतिहासिक शहर है।"),
        cluesEnglish = listOf("It is a historical city."),
        optionsHindi = listOf("मुंबई", "नई दिल्ली", "कोलकाता", "चेन्नई"),
        optionsEnglish = listOf("Mumbai", "New Delhi", "Kolkata", "Chennai"),
        correctAnswerIndex = 1,
        deductionPathHindi = "दिल्ली केंद्र शासित प्रदेश है।",
        deductionPathEnglish = "Delhi is a union territory.",
        eliminationReasonsHindi = listOf("मुंबई वित्तीय राजधानी है", "", "", ""),
        eliminationReasonsEnglish = listOf("Mumbai is financial capital", "", "", ""),
        hintHindi = "यह यमुना नदी के किनारे है।",
        hintEnglish = "It is on the banks of Yamuna.",
        expertAdviceHindi = "ऐतिहासिक तथ्यों पर विचार करें।",
        expertAdviceEnglish = "Consider historical facts.",
        fiftyFiftyDiscardIndices = listOf(0, 2),
        fiftyFiftyProofHindi = "मुंबई और कोलकाता सही नहीं हैं।",
        fiftyFiftyProofEnglish = "Mumbai and Kolkata are incorrect.",
        semanticFingerprint = "test_fp_1"
    )

    @Test
    fun `test user profile default language mode is English`() {
        val profile = UserProfile()
        assertEquals("ENGLISH", profile.languageMode.uppercase())
    }

    @Test
    fun `test null language mode falls back to English`() {
        val resolved = LanguageResolver.validateLanguageMode(null)
        assertEquals("ENGLISH", resolved)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test invalid language mode throws exception`() {
        LanguageResolver.validateLanguageMode("UNKNOWN_LANG")
    }

    @Test
    fun `test Hindi profile resolves Hindi content only`() {
        val resolved = LanguageResolver.resolve(sampleQuestion, "HINDI")
        assertEquals("भारत की राजधानी क्या है?", resolved.questionText)
        assertEquals("नई दिल्ली", resolved.options[1])
        assertEquals("यह यमुना नदी के किनारे है।", resolved.hintText)
        assertFalse(resolved.questionText.contains("capital"))
    }

    @Test
    fun `test English profile resolves English content only`() {
        val resolved = LanguageResolver.resolve(sampleQuestion, "ENGLISH")
        assertEquals("What is the capital of India?", resolved.questionText)
        assertEquals("New Delhi", resolved.options[1])
        assertEquals("It is on the banks of Yamuna.", resolved.hintText)
        assertFalse(resolved.questionText.contains("राजधानी"))
    }

    @Test
    fun `test Bilingual profile resolves both languages paired`() {
        val resolved = LanguageResolver.resolve(sampleQuestion, "BILINGUAL")
        assertTrue(resolved.questionText.contains("भारत की राजधानी क्या है?"))
        assertTrue(resolved.questionText.contains("What is the capital of India?"))
        assertTrue(resolved.options[1].contains("नई दिल्ली"))
        assertTrue(resolved.options[1].contains("New Delhi"))
    }

    @Test
    fun `test language validation ensures correct mapping`() {
        assertTrue(LanguageResolver.validateQuestionContent(sampleQuestion, "HINDI"))
        assertTrue(LanguageResolver.validateQuestionContent(sampleQuestion, "ENGLISH"))
        assertTrue(LanguageResolver.validateQuestionContent(sampleQuestion, "BILINGUAL"))

        val invalidQuestion = sampleQuestion.copy(optionsHindi = listOf("अ")) // invalid size != 4
        assertFalse(LanguageResolver.validateQuestionContent(invalidQuestion, "HINDI"))
    }
}

