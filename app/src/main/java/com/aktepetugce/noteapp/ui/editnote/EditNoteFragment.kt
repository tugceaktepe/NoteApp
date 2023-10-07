package com.aktepetugce.noteapp.ui.editnote

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.aktepetugce.noteapp.NoteApplication
import com.aktepetugce.noteapp.R
import com.aktepetugce.noteapp.databinding.FragmentEditNoteBinding
import com.aktepetugce.noteapp.util.BitmapResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private val safeArgs: EditNoteFragmentArgs by navArgs()

    private val viewModel: EditNoteViewModel by viewModels() {
        EditNoteViewModelFactory(
            (activity?.application as NoteApplication).updateNote,
            (activity?.application as NoteApplication).getNote
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getNote(safeArgs.noteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it.isLoading
            if(!it.errorShown){
                Snackbar.make(requireView(),it.error,Snackbar.LENGTH_LONG).show()
                viewModel.resetError()
            }
            if (it.image != null) {
                binding.editTextTitle.setText(it.title)
                binding.editTextContent.setText(it.content)
                binding.imageViewContent.setImageBitmap(it.image)
            }
            if(it.success){
                Snackbar.make(
                    binding.updateNoteLayout,
                    R.string.note_update_success_msg,
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.imageViewContent.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityLauncher.launch(intent)
        }
        binding.buttonUpdate.setOnClickListener {
            viewModel.update()
        }
    }

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data!!
                lifecycleScope.launch {
                    val selectedImageBitMap = withContext(Dispatchers.IO) {
                        BitmapResolver.getBitmap(
                            requireContext().contentResolver,
                            selectedImageUri
                        )
                    }
                    viewModel.saveSelectedPhoto(selectedImageBitMap)
                }

            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "EditNoteFragment"
    }
}