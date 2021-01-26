package com.example.finalproject.model

import android.os.Parcelable
import androidx.constraintlayout.widget.Placeholder
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val place: String,
    val myEvent: String,
    val myTime: String
): Parcelable
