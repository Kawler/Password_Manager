package com.artemla.passwordmanager.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Password(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "website") val website: String,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "password") val password: String
)
