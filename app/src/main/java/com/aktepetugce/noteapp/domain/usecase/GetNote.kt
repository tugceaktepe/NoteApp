package com.aktepetugce.noteapp.domain.usecase

import com.aktepetugce.noteapp.domain.model.Note
import com.aktepetugce.noteapp.domain.repository.NoteRepository

class GetNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}