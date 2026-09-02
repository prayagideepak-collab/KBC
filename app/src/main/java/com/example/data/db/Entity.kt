package com.example.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "question_registry",
    indices = [
        Index(value = ["questionFingerprint"], unique = true),
        Index(value = ["logicFingerprint"], unique = false)
    ]
)
data class QuestionRegistryEntity(
    @PrimaryKey val id: String,
    val questionId: String,
    val questionFingerprint: String,
    val logicFingerprint: String,
    val canonicalQuestion: String,
    val languageMode: String,
    val difficultyTier: Int,
    val servedBySessionId: String = "",
    val servedByProfileId: String = "",
    val isConsumed: Boolean = true,
    val usedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile_table")
data class UserProfileEntity(
    @PrimaryKey val userId: String = "primary_user",
    val name: String,
    val age: Int,
    val state: String,
    val languageMode: String = "ENGLISH",
    val hostGender: String = "FEMALE",
    val upiId: String? = "",
    val educationLevel: String,
    val occupation: String,
    val preparationDomain: String,
    val studentClass: String = "Class 8",
    val isStudentMode: Boolean = false,
    val interestsJson: String,
    val gkScore: Float,
    val logicScore: Float,
    val historyScore: Float,
    val scienceScore: Float,
    val financeScore: Float,
    val spatialScore: Float,
    val totalGamesPlayed: Int = 0,
    val highestPrizeWon: Long = 0,
    val highestTierReached: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "game_history_table")
data class GameHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: String,
    val finalPrize: Long,
    val grossPrize: Long = 0,
    val totalDeduction: Long = 0,
    val incorrectQuestionsJson: String = "[]",
    val highestQuestionReached: Int,
    val outcomeStatus: String, // "CLEARED_7_CRORE", "LOCKED_CHECKPOINT", "QUIT", "TIME_OUT", "WRONG_ANSWER"
    val correctAnswersCount: Int,
    val lifelinesUsed: String,
    val totalDurationMillis: Long = 0L,
    val totalResponseMillis: Long = 0L,
    val averageResponseMillis: Float = 0f,
    val durationSeconds: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "game_sessions_table")
data class GameSessionEntity(
    @PrimaryKey val sessionId: String,
    val profileId: String,
    val startTimeMillis: Long,
    val endedAtMillis: Long = 0L,
    val status: String, // "ACTIVE", "COMPLETED", "WRONG_ANSWER", "TIMEOUT", "QUIT"
    val finalQuestionReached: Int = 1,
    val finalPrize: Long = 0
)

@Entity(tableName = "game_session_events_table")
data class GameSessionEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val eventType: String, // SESSION_START, QUESTION_SHOWN, READ_START, READ_END, ANSWER_LOCKED, PAUSE_START, PAUSE_END, CORRECT, WRONG, TIMEOUT, QUIT, SESSION_END
    val questionId: String? = null,
    val timestampMillis: Long = System.currentTimeMillis(),
    val metadata: String = ""
)

@Entity(tableName = "app_metadata_table")
data class AppMetadataEntity(
    @PrimaryKey val versionCode: Int,
    val versionName: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val releaseNotes: String
)

@Entity(tableName = "current_affairs_store")
data class CurrentAffairEntity(
    @PrimaryKey val currentAffairId: String,
    val eventId: String,
    val headline: String,
    val canonicalSummary: String,
    val eventDate: String,
    val firstSeenDate: Long = System.currentTimeMillis(),
    val lastVerifiedDate: Long = System.currentTimeMillis(),
    val sourceReferences: String,
    val country: String = "India",
    val state: String = "National",
    val districtRegion: String = "",
    val topic: String,
    val juniorEligibility: Boolean = true,
    val adultEligibility: Boolean = true,
    val minAge: Int = 5,
    val maxAge: Int = 99,
    val examRelevance: String = "All",
    val usedQuestionIdsJson: String = "[]",
    val isExpired: Boolean = false
)

@Entity(
    tableName = "session_question_bank_cache",
    indices = [
        Index(value = ["sessionId"], unique = true),
        Index(value = ["status"], unique = false)
    ]
)
data class SessionQuestionBankCacheEntity(
    @PrimaryKey val sessionId: String,
    val profileId: String,
    val languageMode: String,
    val isJuniorMode: Boolean,
    val status: String, // "PREPARING", "READY", "ACTIVE", "COMPLETED", "FAILED"
    val questionsJson: String, // JSON serialization of all 17 QuestionItems
    val currentAffairEventIdsJson: String = "[]",
    val createdAt: Long = System.currentTimeMillis(),
    val preparedAt: Long = System.currentTimeMillis(),
    val sourceSummary: String = "Online Intelligence & Reasoning Pipeline"
)

