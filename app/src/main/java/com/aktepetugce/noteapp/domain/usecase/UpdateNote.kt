package com.aktepetugce.noteapp.domain.usecase

import com.aktepetugce.noteapp.domain.model.Note
import com.aktepetugce.noteapp.domain.repository.NoteRepository
import com.aktepetugce.noteapp.util.Result
import com.aktepetugce.noteapp.util.toResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNote(
    private val repository: NoteRepository,
) {
    operator fun invoke(note: Note): Flow<Result<Unit>> = flow {
        emit(repository.updateNote(note))
    }.toResult()
}