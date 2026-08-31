package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        QuestionRegistryEntity::class,
        UserProfileEntity::class,
        GameHistoryEntity::class,
        GameSessionEntity::class,
        GameSessionEventEntity::class,
        AppMetadataEntity::class,
        CurrentAffairEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class TarkDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun gameHistoryDao(): GameHistoryDao
    abstract fun gameSessionDao(): GameSessionDao
    abstract fun appMetadataDao(): AppMetadataDao
    abstract fun currentAffairsDao(): CurrentAffairsDao

    companion object {
        @Volatile
        private var INSTANCE: TarkDatabase? = null

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `app_metadata_table` (
                        `versionCode` INTEGER NOT NULL,
                        `versionName` TEXT NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        `releaseNotes` TEXT NOT NULL,
                        PRIMARY KEY(`versionCode`)
                    )
                """)
                database.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (6, '1.1.0', ${System.currentTimeMillis()}, 'TarkShastra Authoritative Version Authority & Session Foundation')
                """)

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `game_sessions_table` (
                        `sessionId` TEXT NOT NULL,
                        `profileId` TEXT NOT NULL,
                        `startTimeMillis` INTEGER NOT NULL,
                        `endedAtMillis` INTEGER NOT NULL,
                        `status` TEXT NOT NULL,
                        `finalQuestionReached` INTEGER NOT NULL,
                        `finalPrize` INTEGER NOT NULL,
                        PRIMARY KEY(`sessionId`)
                    )
                """)

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `game_session_events_table` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `sessionId` TEXT NOT NULL,
                        `eventType` TEXT NOT NULL,
                        `questionId` TEXT,
                        `timestampMillis` INTEGER NOT NULL,
                        `metadata` TEXT NOT NULL
                    )
                """)

                database.execSQL("ALTER TABLE user_profile_table ADD COLUMN languageMode TEXT NOT NULL DEFAULT 'HINDI'")
                database.execSQL("ALTER TABLE user_profile_table ADD COLUMN upiId TEXT")

                database.execSQL("ALTER TABLE game_history_table ADD COLUMN totalDurationMillis INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE game_history_table ADD COLUMN totalResponseMillis INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE game_history_table ADD COLUMN averageResponseMillis REAL NOT NULL DEFAULT 0.0")

                database.execSQL("DROP TABLE IF EXISTS `question_registry_new`")
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `question_registry` (
                        `id` TEXT NOT NULL,
                        `profileId` TEXT NOT NULL,
                        `questionId` TEXT NOT NULL,
                        `semanticFingerprint` TEXT NOT NULL,
                        `canonicalQuestion` TEXT NOT NULL,
                        `languageMode` TEXT NOT NULL,
                        `difficultyTier` INTEGER NOT NULL,
                        `questionVersion` INTEGER NOT NULL,
                        `usedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """)
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_question_registry_profileId_semanticFingerprint` ON `question_registry` (`profileId`, `semanticFingerprint`)")
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (7, '1.2.0', ${System.currentTimeMillis()}, 'Junior Reading and Timer State Machine Authoritative Update')
                """)
            }
        }

        fun getDatabase(context: Context): TarkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TarkDatabase::class.java,
                    "tark_shastra_database.db"
                )
                    .addMigrations(MIGRATION_5_6, MIGRATION_6_7)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
