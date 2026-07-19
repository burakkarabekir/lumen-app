package com.bksd.journal.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

@Database(entities = [MomentEntity::class], version = 5)
@ConstructedBy(JournalDatabaseConstructor::class)
abstract class JournalDatabase : RoomDatabase() {
    abstract fun momentDao(): MomentDao
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE moments ADD COLUMN pendingUpload INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "CREATE TABLE `moments_new` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `body` TEXT, `createdAtMs` INTEGER NOT NULL, `moods` TEXT NOT NULL, `tags` TEXT NOT NULL, `locationLatitude` REAL, `locationLongitude` REAL, `locationDisplayName` TEXT, `attachments` TEXT NOT NULL, `isFavorite` INTEGER NOT NULL, `pendingSync` INTEGER NOT NULL, `pendingDelete` INTEGER NOT NULL, `pendingUpload` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`id`))"
        )
        connection.execSQL(
            "INSERT INTO `moments_new` (`id`, `title`, `body`, `createdAtMs`, `moods`, `tags`, `locationLatitude`, `locationLongitude`, `locationDisplayName`, `attachments`, `isFavorite`, `pendingSync`, `pendingDelete`, `pendingUpload`) SELECT `id`, `title`, `body`, `createdAtMs`, `moods`, `tags`, `locationLatitude`, `locationLongitude`, `locationDisplayName`, `attachments`, `isFavorite`, `pendingSync`, `pendingDelete`, `pendingUpload` FROM `moments`"
        )
        connection.execSQL("DROP TABLE `moments`")
        connection.execSQL("ALTER TABLE `moments_new` RENAME TO `moments`")
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object JournalDatabaseConstructor : RoomDatabaseConstructor<JournalDatabase> {
    override fun initialize(): JournalDatabase
}
