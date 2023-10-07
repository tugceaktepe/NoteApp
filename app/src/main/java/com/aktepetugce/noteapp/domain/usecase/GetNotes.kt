package com.aktepetugce.noteapp.domain.usecase

import com.aktepetugce.noteapp.domain.model.Note
import com.aktepetugce.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotes(
    private val repository: NoteRepository
) {

    operator fun invoke(): Flow<List<Note>> = repository.getNotes()
}