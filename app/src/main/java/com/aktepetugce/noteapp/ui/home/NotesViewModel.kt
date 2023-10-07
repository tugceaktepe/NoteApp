package com.aktepetugce.noteapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aktepetugce.noteapp.domain.model.Note
import com.aktepetugce.noteapp.domain.usecase.DeleteNote
import com.aktepetugce.noteapp.domain.usecase.GetNote
import com.aktepetugce.noteapp.domain.usecase.GetNotes
import kotlinx.coroutines.launch

class NotesViewModel(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val getNote: GetNote,
) : ViewModel() {

    private var _uiState = MutableLiveData(NotesUiState())
    val uiState: LiveData<NotesUiState> = _uiState

    fun getAllNotes() = viewModelScope.launch {
        if(uiState.value?.isLoaded == true) {
            return@launch
        }
        getNotes().collect {
            _uiState.value = _uiState.value?.copy(
                allNotes = it,
                isLoaded = true
            )
        }
    }

    fun delete(noteId: Int) = viewModelScope.launch {
        val note = getNote(noteId)
        note?.let {
            deleteNote(note)
            _uiState.value = uiState.value?.copy(
                allNotes = getNotes().asLiveData().value ?: emptyList()
            )
        }
    }
}


data class NotesUiState(
    val allNotes: List<Note> = emptyList(),
    val isLoaded: Boolean = false,
)


@Suppress("UNCHECKED_CAST")
class NotesViewModelFactory(
    private val getNotes: GetNotes,
    private val deleteNote: DeleteNote,
    private val getNote: GetNote,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(getNotes, deleteNote, getNote) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
