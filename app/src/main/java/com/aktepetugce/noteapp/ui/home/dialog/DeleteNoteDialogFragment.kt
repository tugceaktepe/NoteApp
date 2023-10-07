package com.aktepetugce.noteapp.ui.home.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.aktepetugce.noteapp.R
import com.aktepetugce.noteapp.databinding.FragmentDialogDeleteNoteBinding
import com.aktepetugce.noteapp.ui.home.NotesViewModel

class DeleteNoteDialogFragment : DialogFragment() {

    private var _binding: FragmentDialogDeleteNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotesViewModel by viewModels({ requireParentFragment() })
    private var noteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            noteId = it.getInt(NOTE_ID, 0)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDialogDeleteNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.deleteYesButton.setOnClickListener {
            viewModel.delete(noteId)
        }
        binding.deleteNoButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setWindowAnimations(
            R.style.dialog_animation_fade
        );
    }

    companion object {
        private const val NOTE_ID = "note_id"

        fun newInstance(noteId: Int): DeleteNoteDialogFragment = DeleteNoteDialogFragment().apply {
            arguments = Bundle().apply {
                putInt(NOTE_ID, noteId)
            }
        }
    }

}