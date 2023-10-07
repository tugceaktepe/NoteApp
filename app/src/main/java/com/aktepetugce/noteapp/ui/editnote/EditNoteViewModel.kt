package com.aktepetugce.noteapp.ui.editnote

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aktepetugce.noteapp.domain.usecase.GetNote
import com.aktepetugce.noteapp.domain.usecase.UpdateNote
import com.aktepetugce.noteapp.util.Result
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditNoteViewModel(
    private val updateNote: UpdateNote,
    private val getNoteById: GetNote,
) : ViewModel() {
    private val _uiState = MutableLiveData<EditNoteUiState>()
    val uiState: LiveData<EditNoteUiState> = _uiState

    fun getNote(id: Int) = viewModelScope.launch {
        val note = getNoteById(id)
        note?.let {
            _uiState.value = EditNoteUiState(
                id = it.id,
                image = it.image,
                title = it.title,
                content = it.content
            )
        }
    }

    fun update() = viewModelScope.launch {
        getNoteById(uiState.value?.id!!)?.let {
            updateNote(
                it.copy(
                    title = uiState.value?.title!!,
                    content = uiState.value?.content!!,
                    image = uiState.value?.image!!,
                    modified = FORMATTER.format(Date()),
                    isEdited = true
                )
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value?.copy(
                            isLoading = true
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value?.copy(
                            isLoading = false,
                            error = result.message,
                            errorShown = false
                        )
                    }

                    is Result.Success<*> -> {
                        _uiState.value = _uiState.value?.copy(
                            isLoading = false,
                            success = true
                        )
                    }
                }
            }
        }
    }

    fun saveSelectedPhoto(selectedImage: Bitmap?) {
        _uiState.value = _uiState.value?.copy(
            image = selectedImage
        )
    }

    fun resetError() {
        _uiState.value = _uiState.value?.copy(
            error = "",
            errorShown = true
        )
    }

    data class EditNoteUiState(
        val id: Int,
        val image: Bitmap?,
        val title: String,
        val content: String,
        val isLoading: Boolean = false,
        val error: String = "",
        val errorShown: Boolean = true,
        val success: Boolean = false,
    )

    companion object {
        private val FORMATTER = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("tr-TR"))
    }

}


@Suppress("UNCHECKED_CAST")
class EditNoteViewModelFactory(
    private val updateNote: UpdateNote,
    private val getNoteById: GetNote,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNoteViewModel::class.java)) {
            return EditNoteViewModel(updateNote, getNoteById) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}