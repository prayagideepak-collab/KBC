package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_registry")
data class QuestionRegistryEntity(
    @PrimaryKey val id: String,
    val semanticFingerprint: String,
    val canonicalQuestion: String,
    val category: String,
    val difficultyTier: Int,
    val correctAnswer: String,
    val deductionSummary: String,
    val isFlipped: Boolean = false,
    val servedCount: Int = 1,
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile_table")
data class UserProfileEntity(
    @PrimaryKey val userId: String = "primary_user",
    val name: String,
    val age: Int,
    val state: String,
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
    val highestQuestionReached: Int,
    val outcomeStatus: String, // "CLEARED_7_CRORE", "LOCKED_CHECKPOINT", "QUIT", "TIME_OUT", "WRONG_ANSWER"
    val correctAnswersCount: Int,
    val lifelinesUsed: String,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
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
    val topic: String, // "National", "International", "Govt Schemes", "Science & Tech", "Environment", "Economy", "Sports", "Education", "Appointments", "Regional"
    val juniorEligibility: Boolean = true,
    val adultEligibility: Boolean = true,
    val minAge: Int = 5,
    val maxAge: Int = 99,
    val examRelevance: String = "All",
    val usedQuestionIdsJson: String = "[]",
    val isExpired: Boolean = false
)
