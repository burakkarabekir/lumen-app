package com.bksd.journal.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(entities = [MomentEntity::class], version = 1)
@ConstructedBy(JournalDatabaseConstructor::class)
abstract class JournalDatabase : RoomDatabase() {
    abstract fun momentDao(): MomentDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object JournalDatabaseConstructor : RoomDatabaseConstructor<JournalDatabase> {
    override fun initialize(): JournalDatabase
}
