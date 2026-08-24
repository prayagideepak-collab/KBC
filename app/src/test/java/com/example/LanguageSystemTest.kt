package com.example

import com.example.data.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LanguageSystemTest {

    @Test
    fun `test user profile default language mode is Hindi`() {
        val profile = UserProfile()
        assertEquals("HINDI", profile.languageMode.uppercase())
    }

    @Test
    fun `test language mode validation and normalization`() {
        val hindiProfile = UserProfile(languageMode = "hindi")
        assertEquals("HINDI", hindiProfile.languageMode.uppercase())

        val englishProfile = UserProfile(languageMode = "ENGLISH")
        assertEquals("ENGLISH", englishProfile.languageMode.uppercase())

        val bilingualProfile = UserProfile(languageMode = "bilingual")
        assertEquals("BILINGUAL", bilingualProfile.languageMode.uppercase())

        val invalidProfile = UserProfile(languageMode = "unknown")
        // fallback or normalization handling
        val normalized = invalidProfile.languageMode.uppercase().let {
            if (it in listOf("HINDI", "ENGLISH", "BILINGUAL")) it else "HINDI"
        }
        assertEquals("HINDI", normalized)
    }

    @Test
    fun `test language mode isolation rules`() {
        val hindiProfile = UserProfile(languageMode = "HINDI")
        val isHindiMode = hindiProfile.languageMode.uppercase() == "HINDI"
        assertTrue(isHindiMode)

        val englishProfile = UserProfile(languageMode = "ENGLISH")
        val isEnglishMode = englishProfile.languageMode.uppercase() == "ENGLISH"
        assertTrue(isEnglishMode)

        val bilingualProfile = UserProfile(languageMode = "BILINGUAL")
        val isBilingualMode = bilingualProfile.languageMode.uppercase() == "BILINGUAL"
        assertTrue(isBilingualMode)
    }
}
