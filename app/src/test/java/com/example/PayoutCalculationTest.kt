package com.example

import com.example.data.api.IncorrectDeductionDto
import com.example.data.api.PayoutReportDto
import com.example.data.model.GameSessionResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PayoutCalculationTest {

    @Test
    fun testZeroFloorDeduction() {
        val gross = 200L
        val deduction = 800L
        val finalAmount = maxOf(0L, gross - deduction)

        assertEquals(0L, finalAmount)
    }

    @Test
    fun testNormalDeduction() {
        val gross = 1000L
        val deduction = 300L
        val finalAmount = maxOf(0L, gross - deduction)

        assertEquals(700L, finalAmount)
    }

    @Test
    fun testNoDeduction() {
        val gross = 500L
        val deduction = 0L
        val finalAmount = maxOf(0L, gross - deduction)

        assertEquals(500L, finalAmount)
    }

    @Test
    fun testGameSessionResultStructure() {
        val result = GameSessionResult(
            sessionId = "test-123",
            userName = "Test User",
            totalPointsWon = 700L, // final
            grossWinningAmount = 1000L,
            totalNegativeDeduction = 300L,
            incorrectQuestionDeductionsJson = "[]",
            highestQuestionReached = 10,
            isCompletedWon = false,
            guaranteedPointsSecured = 1000L,
            reasonEnded = "QUIT",
            questionsAnsweredCount = 10,
            correctCount = 9,
            wrongCount = 1,
            lifelinesUsedCount = 0,
            averageResponseTimeSec = 10f,
            logicAccuracyPercentage = 90
        )
        
        assertEquals(700L, result.totalPointsWon)
        assertEquals(1000L, result.grossWinningAmount)
        assertEquals(300L, result.totalNegativeDeduction)
    }

    @Test
    fun testPayoutReportDtoCreation() {
        val dto = PayoutReportDto(
            userName = "Test User",
            upiId = "test@upi",
            grossWinningAmount = 1000L,
            correctAnswers = 15,
            incorrectAnswers = 2,
            negativeDeduction = 800L,
            finalWinningAmount = 200L,
            incorrectQuestionDeductions = listOf(
                IncorrectDeductionDto(16, 400L),
                IncorrectDeductionDto(17, 400L)
            ),
            resultId = "session-123"
        )
        
        assertEquals("Test User", dto.userName)
        assertEquals("test@upi", dto.upiId)
        assertEquals(1000L, dto.grossWinningAmount)
        assertEquals(200L, dto.finalWinningAmount)
        assertEquals(2, dto.incorrectQuestionDeductions.size)
    }
}
