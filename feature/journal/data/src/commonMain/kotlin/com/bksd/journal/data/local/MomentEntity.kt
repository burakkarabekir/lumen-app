package com.bksd.journal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moments")
data class MomentEntity(
    @PrimaryKey val id: String,
    val body: String?,
    val createdAtMs: Long,
    val mood: String,
    val tags: String,
    val locationLatitude: Double?,
    val locationLongitude: Double?,
    val locationDisplayName: String?,
    val attachments: String,
    val pendingSync: Boolean = false
)
