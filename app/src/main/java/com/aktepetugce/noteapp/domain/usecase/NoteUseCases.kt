package com.aktepetugce.noteapp.domain.usecase

data class NoteUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote,
    val updateNote: UpdateNote
)