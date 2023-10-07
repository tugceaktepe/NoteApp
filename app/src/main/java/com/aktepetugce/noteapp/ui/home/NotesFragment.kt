package com.aktepetugce.noteapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aktepetugce.noteapp.NoteApplication
import com.aktepetugce.noteapp.R
import com.aktepetugce.noteapp.databinding.FragmentNotesBinding
import com.aktepetugce.noteapp.ui.home.dialog.DeleteNoteDialogFragment

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoteAdapter

    private val viewModel: NotesViewModel by viewModels() {
        NotesViewModelFactory(
            (activity?.application as NoteApplication).getNotes,
            (activity?.application as NoteApplication).deleteNote,
            (activity?.application as NoteApplication).getNote
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllNotes()
        initUi()
    }

    private fun initUi() {
        adapter = NoteAdapter(
            clickNote = {
                val action = NotesFragmentDirections
                    .actionNotesFragmentToEditNoteFragment(it.id)
                findNavController().navigate(action)
            }, longClickNote = {
                val dialog = DeleteNoteDialogFragment.newInstance(it.id)
                dialog.show(childFragmentManager, DIALOG_TAG)
            })
        binding.notesRecyclerView.adapter = adapter
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            childFragmentManager.findFragmentByTag(DIALOG_TAG)?.let {
                if (it.isAdded && it is DialogFragment) {
                    it.dismiss()
                }
            }
            binding.noNotesTextView.isVisible = uiState.allNotes.isEmpty()
            binding.notesRecyclerView.isVisible = uiState.allNotes.isNotEmpty()
            adapter.submitList(uiState.allNotes)
        }
        binding.buttonAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_addNoteFragment)
        }
    }

    companion object {
        const val DIALOG_TAG = "DELETE_DIALOG"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}