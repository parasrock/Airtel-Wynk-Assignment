package com.paras.baseapplication.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "response")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "response") var response: String? = null,
    @ColumnInfo(name = "created_at") var created_at: Long = Calendar.getInstance().timeInMillis
)