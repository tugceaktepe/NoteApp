package com.aktepetugce.noteapp.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aktepetugce.noteapp.data.migrations.migration_1_2
import com.aktepetugce.noteapp.data.source.NoteDatabase.Companion.DATABASE_VERSION
import com.aktepetugce.noteapp.domain.model.Note

@Database(
    entities = [Note::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(DateConverter::class, BitmapConverter::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_VERSION = 3
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).addMigrations(migration_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}