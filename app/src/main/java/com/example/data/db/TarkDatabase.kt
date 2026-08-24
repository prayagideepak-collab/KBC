package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        QuestionRegistryEntity::class,
        QuestionHistoryEntity::class,
        UserProfileEntity::class,
        GameHistoryEntity::class,
        GameSessionEntity::class,
        GameSessionEventEntity::class,
        AppMetadataEntity::class,
        CurrentAffairEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class TarkDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun questionHistoryDao(): QuestionHistoryDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun gameHistoryDao(): GameHistoryDao
    abstract fun gameSessionDao(): GameSessionDao
    abstract fun appMetadataDao(): AppMetadataDao
    abstract fun currentAffairsDao(): CurrentAffairsDao

    companion object {
        @Volatile
        private var INSTANCE: TarkDatabase? = null

        fun getDatabase(context: Context): TarkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TarkDatabase::class.java,
                    "tark_shastra_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
