package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM question_registry WHERE semanticFingerprint = :fingerprint LIMIT 1")
    suspend fun getQuestionByFingerprint(fingerprint: String): QuestionRegistryEntity?

    @Query("SELECT semanticFingerprint FROM question_registry")
    suspend fun getAllServedFingerprints(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerQuestion(entity: QuestionRegistryEntity)

    @Query("UPDATE question_registry SET isFlipped = 1 WHERE id = :questionId")
    suspend fun markQuestionFlipped(questionId: String)

    @Query("SELECT COUNT(*) FROM question_registry")
    fun getRegisteredQuestionsCount(): Flow<Int>
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile_table WHERE userId = :userId LIMIT 1")
    fun getUserProfileFlow(userId: String = "primary_user"): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile_table WHERE userId = :userId LIMIT 1")
    suspend fun getUserProfile(userId: String = "primary_user"): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(entity: UserProfileEntity)
}

@Dao
interface GameHistoryDao {
    @Query("SELECT * FROM game_history_table ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<GameHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameHistory(entity: GameHistoryEntity)

    @Query("SELECT MAX(finalPrize) FROM game_history_table")
    suspend fun getHighestPrizeWon(): Long?

    @Query("SELECT MAX(highestQuestionReached) FROM game_history_table")
    suspend fun getHighestQuestionReached(): Int?
}

@Dao
interface CurrentAffairsDao {
    @Query("SELECT * FROM current_affairs_store WHERE isExpired = 0 ORDER BY lastVerifiedDate DESC")
    fun getAllActiveCurrentAffairsFlow(): Flow<List<CurrentAffairEntity>>

    @Query("SELECT * FROM current_affairs_store WHERE isExpired = 0 ORDER BY lastVerifiedDate DESC")
    suspend fun getAllActiveCurrentAffairs(): List<CurrentAffairEntity>

    @Query("SELECT * FROM current_affairs_store WHERE juniorEligibility = 1 AND isExpired = 0 AND minAge <= :age AND maxAge >= :age ORDER BY lastVerifiedDate DESC")
    suspend fun getJuniorEligibleAffairs(age: Int): List<CurrentAffairEntity>

    @Query("SELECT * FROM current_affairs_store WHERE adultEligibility = 1 AND isExpired = 0 ORDER BY lastVerifiedDate DESC")
    suspend fun getAdultEligibleAffairs(): List<CurrentAffairEntity>

    @Query("SELECT * FROM current_affairs_store WHERE (state = :state OR state = 'National' OR state = 'India') AND isExpired = 0 ORDER BY lastVerifiedDate DESC")
    suspend fun getAffairsByStateOrNational(state: String): List<CurrentAffairEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAffairs(affairs: List<CurrentAffairEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAffair(affair: CurrentAffairEntity)

    @Query("SELECT COUNT(*) FROM current_affairs_store")
    suspend fun getCount(): Int

    @Query("UPDATE current_affairs_store SET usedQuestionIdsJson = :usedJson WHERE currentAffairId = :affairId")
    suspend fun updateUsedQuestion(affairId: String, usedJson: String)
}
