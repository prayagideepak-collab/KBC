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
        CurrentAffairEntity::class,
        SessionQuestionBankCacheEntity::class
    ],
    version = 15,
    exportSchema = false
)
abstract class TarkDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun gameHistoryDao(): GameHistoryDao
    abstract fun gameSessionDao(): GameSessionDao
    abstract fun appMetadataDao(): AppMetadataDao
    abstract fun currentAffairsDao(): CurrentAffairsDao
    abstract fun sessionBankCacheDao(): SessionBankCacheDao

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

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (8, '1.3.0', ${System.currentTimeMillis()}, 'Cycle 2 State Machine, Atomic TTS, and Authoritative Timer Architecture')
                """)
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `question_registry_new` (
                        `id` TEXT NOT NULL,
                        `questionId` TEXT NOT NULL,
                        `questionFingerprint` TEXT NOT NULL,
                        `logicFingerprint` TEXT NOT NULL,
                        `canonicalQuestion` TEXT NOT NULL,
                        `languageMode` TEXT NOT NULL,
                        `difficultyTier` INTEGER NOT NULL,
                        `servedBySessionId` TEXT NOT NULL DEFAULT '',
                        `servedByProfileId` TEXT NOT NULL DEFAULT '',
                        `isConsumed` INTEGER NOT NULL DEFAULT 1,
                        `usedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """)
                database.execSQL("""
                    INSERT OR IGNORE INTO `question_registry_new` (id, questionId, questionFingerprint, logicFingerprint, canonicalQuestion, languageMode, difficultyTier, servedByProfileId, usedAt)
                    SELECT id, questionId, semanticFingerprint, 'legacy_logic_' || difficultyTier, canonicalQuestion, languageMode, difficultyTier, profileId, usedAt
                    FROM `question_registry`
                """)
                database.execSQL("DROP TABLE IF EXISTS `question_registry`")
                database.execSQL("ALTER TABLE `question_registry_new` RENAME TO `question_registry`")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_question_registry_questionFingerprint` ON `question_registry` (`questionFingerprint`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_question_registry_logicFingerprint` ON `question_registry` (`logicFingerprint`)")

                database.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (9, '1.4.0', ${System.currentTimeMillis()}, 'Global Permanent Question Uniqueness & Authoritative Registry')
                """)
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    UPDATE user_profile_table 
                    SET languageMode = 'ENGLISH' 
                    WHERE languageMode NOT IN ('HINDI', 'ENGLISH', 'BILINGUAL') OR languageMode IS NULL OR languageMode = ''
                """)
                database.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (10, '1.5.0', ${System.currentTimeMillis()}, 'English Language Default Migration')
                """)
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (11, '1.6.0', ${System.currentTimeMillis()}, 'Final Challenger Default Profile & Category Personalization')
                """)
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `session_question_bank_cache` (
                        `sessionId` TEXT NOT NULL,
                        `profileId` TEXT NOT NULL,
                        `languageMode` TEXT NOT NULL,
                        `isJuniorMode` INTEGER NOT NULL,
                        `status` TEXT NOT NULL,
                        `questionsJson` TEXT NOT NULL,
                        `currentAffairEventIdsJson` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `preparedAt` INTEGER NOT NULL,
                        `sourceSummary` TEXT NOT NULL,
                        PRIMARY KEY(`sessionId`)
                    )
                """)
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_session_question_bank_cache_sessionId` ON `session_question_bank_cache` (`sessionId`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_session_question_bank_cache_status` ON `session_question_bank_cache` (`status`)")

                database.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (12, '1.7.0', ${System.currentTimeMillis()}, 'Online Question Intelligence, Local Registry, and Offline Session Cache Architecture')
                """)
            }
        }

        val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user_profile_table ADD COLUMN hostGender TEXT NOT NULL DEFAULT 'FEMALE'")
                database.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (13, '1.8.0', ${System.currentTimeMillis()}, 'Host Gender Selection')
                """)
            }
        }

        val MIGRATION_13_14 = object : Migration(13, 14) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE game_history_table ADD COLUMN grossPrize INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE game_history_table ADD COLUMN totalDeduction INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE game_history_table ADD COLUMN incorrectQuestionsJson TEXT NOT NULL DEFAULT '[]'")
                database.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (14, '1.9.0', ${System.currentTimeMillis()}, 'Financial Breakdown and Payout Rules')
                """)
            }
        }

        val MIGRATION_14_15 = object : Migration(14, 15) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1. User profile city
                db.execSQL("ALTER TABLE user_profile_table ADD COLUMN city TEXT NOT NULL DEFAULT 'Lucknow'")

                // 2. Question registry multi-layer fingerprints & generation version
                db.execSQL("ALTER TABLE question_registry ADD COLUMN semanticFingerprint TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE question_registry ADD COLUMN conceptFingerprint TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE question_registry ADD COLUMN patternFingerprint TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE question_registry ADD COLUMN generationVersion INTEGER NOT NULL DEFAULT 2")
                db.execSQL("ALTER TABLE question_registry ADD COLUMN normalizedQuestionText TEXT NOT NULL DEFAULT ''")

                // 3. Session Question Bank Cache: Hard Reset fields & invalidate all existing caches
                db.execSQL("ALTER TABLE session_question_bank_cache ADD COLUMN state TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE session_question_bank_cache ADD COLUMN city TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE session_question_bank_cache ADD COLUMN studentClass TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE session_question_bank_cache ADD COLUMN categoriesJson TEXT NOT NULL DEFAULT '[]'")
                db.execSQL("ALTER TABLE session_question_bank_cache ADD COLUMN preparationDomain TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE session_question_bank_cache ADD COLUMN generationVersion INTEGER NOT NULL DEFAULT 2")
                db.execSQL("ALTER TABLE session_question_bank_cache ADD COLUMN sourceVersion TEXT NOT NULL DEFAULT 'GEN_V2_AUTHORITATIVE'")
                db.execSQL("ALTER TABLE session_question_bank_cache ADD COLUMN configHash TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE session_question_bank_cache ADD COLUMN isInvalidated INTEGER NOT NULL DEFAULT 0")

                // AUTHORITATIVE HARD RESET: Invalidate all existing session banks!
                db.execSQL("UPDATE session_question_bank_cache SET status = 'INVALIDATED', isInvalidated = 1")

                db.execSQL("""
                    INSERT OR REPLACE INTO `app_metadata_table` (`versionCode`, `versionName`, `updatedAt`, `releaseNotes`)
                    VALUES (15, '2.0.0', ${System.currentTimeMillis()}, 'Fresh Question Architecture and Authoritative Hard Reset')
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
                    .addMigrations(MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12, MIGRATION_12_13, MIGRATION_13_14, MIGRATION_14_15)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
