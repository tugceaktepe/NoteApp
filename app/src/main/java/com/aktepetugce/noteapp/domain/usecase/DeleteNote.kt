package com.aktepetugce.noteapp.domain.usecase

import com.aktepetugce.noteapp.domain.model.Note
import com.aktepetugce.noteapp.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}