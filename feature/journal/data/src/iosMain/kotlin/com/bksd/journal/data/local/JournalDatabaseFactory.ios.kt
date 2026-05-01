package com.bksd.journal.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getJournalDatabaseBuilder(): RoomDatabase.Builder<JournalDatabase> {
    val dbFilePath = NSHomeDirectory() + "/Documents/journal.db"
    return Room.databaseBuilder<JournalDatabase>(
        name = dbFilePath
    )
}
