package com.aktepetugce.noteapp.domain.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "created")
    val created: String,
    @ColumnInfo(name = "modified")
    val modified: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "text")
    val content: String,
    @ColumnInfo(name = "image")
    val image: Bitmap,
    @ColumnInfo(name = "isEdited")
    val isEdited: Boolean
)

class InvalidNoteException(message: String) : Exception(message)