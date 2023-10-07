package com.aktepetugce.noteapp

import android.app.Application
import com.aktepetugce.noteapp.data.repository.NoteRepositoryImpl
import com.aktepetugce.noteapp.data.source.NoteDatabase
import com.aktepetugce.noteapp.domain.usecase.AddNote
import com.aktepetugce.noteapp.domain.usecase.DeleteNote
import com.aktepetugce.noteapp.domain.usecase.GetNote
import com.aktepetugce.noteapp.domain.usecase.GetNotes
import com.aktepetugce.noteapp.domain.usecase.NoteUseCases
import com.aktepetugce.noteapp.domain.usecase.UpdateNote

class NoteApplication : Application() {
    private val database by lazy { NoteDatabase.getDatabase(this) }
    private val repository by lazy { NoteRepositoryImpl(database.noteDao()) }
    private val useCases by lazy { NoteUseCases(
        getNotes = GetNotes(repository),
        deleteNote = DeleteNote(repository),
        addNote = AddNote(repository),
        getNote = GetNote(repository),
        updateNote = UpdateNote(repository)
        )
     }
    val getNotes by lazy { useCases.getNotes }
    val deleteNote by lazy { useCases.deleteNote }
    val addNote by lazy { useCases.addNote }
    val getNote by lazy { useCases.getNote }
    val updateNote by lazy { useCases.updateNote }
}