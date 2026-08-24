package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        QuestionRegistryEntity::class,
        UserProfileEntity::class,
        GameHistoryEntity::class,
        CurrentAffairEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class TarkDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun gameHistoryDao(): GameHistoryDao
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
