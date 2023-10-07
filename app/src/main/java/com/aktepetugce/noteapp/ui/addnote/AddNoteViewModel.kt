package com.aktepetugce.noteapp.ui.addnote

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aktepetugce.noteapp.domain.model.Note
import com.aktepetugce.noteapp.domain.usecase.AddNote
import com.aktepetugce.noteapp.util.Result
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteViewModel(val addNote: AddNote) : ViewModel() {
    private val _uiState = MutableLiveData(AddNoteUiState())
    val uiState: LiveData<AddNoteUiState> = _uiState

    fun addNote(
        title: String,
        content: String,
        image: Bitmap,
    ) = viewModelScope.launch {
        val date = Date()
        val note = Note(
            title = title,
            content = content,
            image = image,
            created = FORMATTER.format(date),
            modified = FORMATTER.format(date),
            isEdited = false
        )
        addNote(note).collect{
            when(it){
                is Result.Loading -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = true
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        error = it.message,
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

    fun saveSelectedPhoto(selectedImage: Bitmap) = viewModelScope.launch {
        _uiState.value = _uiState.value?.copy(
            image = selectedImage
        )
    }

    fun resetError(){
        _uiState.value = _uiState.value?.copy(
            error = "",
            errorShown = true
        )
    }

    data class AddNoteUiState(
        val image: Bitmap? = null,
        val isLoading: Boolean = false,
        val error: String = "",
        val errorShown : Boolean = true,
        val success: Boolean = false
    )

    companion object {
        private val FORMATTER = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("tr-TR"))
    }
}


@Suppress("UNCHECKED_CAST")
class AddNoteViewModelFactory(private val addNote: AddNote) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)) {
            return AddNoteViewModel(addNote) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
