package com.bksd.journal.data.local

import androidx.room.RoomDatabase

expect fun getJournalDatabaseBuilder(): RoomDatabase.Builder<JournalDatabase>
