package com.bksd.journal.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.mp.KoinPlatform

actual fun getJournalDatabaseBuilder(): RoomDatabase.Builder<JournalDatabase> {
    val context = KoinPlatform.getKoin().get<Context>()
    val dbFile = context.getDatabasePath("journal.db")
    return Room.databaseBuilder<JournalDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}
