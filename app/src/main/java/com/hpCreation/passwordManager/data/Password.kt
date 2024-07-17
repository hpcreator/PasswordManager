package com.hpCreation.passwordManager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class Password(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val accountType: String,
    @ColumnInfo val username: String,
    @ColumnInfo val password: String
)