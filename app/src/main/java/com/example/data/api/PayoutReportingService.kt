package com.example.data.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class PayoutReportDto(
    val userName: String,
    val upiId: String,
    val pointsWon: Long,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val resultId: String
)

interface PayoutReportingService {
    suspend fun reportPayout(payload: PayoutReportDto): Boolean
}

class DefaultPayoutReportingService : PayoutReportingService {
    override suspend fun reportPayout(payload: PayoutReportDto): Boolean = withContext(Dispatchers.IO) {
        try {
            // Validation: must have non-empty UPI ID
            val trimmedUpi = payload.upiId.trim()
            if (trimmedUpi.isEmpty() || !trimmedUpi.contains("@")) {
                Log.e("PayoutReporting", "Invalid UPI ID format. Aborting report.")
                return@withContext false
            }

            // Zero-floor check: pointsWon must never be negative
            if (payload.pointsWon < 0) {
                Log.e("PayoutReporting", "Negative points won detected. Aborting report.")
                return@withContext false
            }

            // MOCK HTTPS BACKEND CALL
            // In a real application, this would use Retrofit/Ktor over HTTPS
            Log.d("PayoutReporting", "Securely transmitting payout record to backend for Result ID: ${payload.resultId}")
            
            // Simulating successful network request
            true
        } catch (e: Exception) {
            Log.e("PayoutReporting", "Failed to transmit payout report", e)
            false
        }
    }
}
