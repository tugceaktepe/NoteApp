package com.aktepetugce.noteapp.data.source

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Locale

object DateConverter {
    @TypeConverter
    fun fromTimestamp(timeStamp: Long?): String? {
        return timeStamp?.let {
            FORMATTER.format(timeStamp)
        }
    }

    @TypeConverter
    fun dateToTimestamp(timeStamp: String?): Long? {
        return timeStamp?.let {
            FORMATTER.parse(it)?.time
        }
    }

    //TODO: get locale from device settings
    private val FORMATTER = SimpleDateFormat("dd/MM/yyyy", Locale("tr-TR"))
}