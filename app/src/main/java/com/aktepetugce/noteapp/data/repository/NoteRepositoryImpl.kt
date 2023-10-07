package com.aktepetugce.noteapp.data.repository

import com.aktepetugce.noteapp.data.source.NoteDao
import com.aktepetugce.noteapp.domain.model.Note
import com.aktepetugce.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository{
    override fun getNotes(): Flow<List<Note>> {
        return dao.getAllNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun addNote(note: Note) {
        dao.addNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

    override suspend fun updateNote(note: Note) {
        dao.updateNote(note)
    }

}