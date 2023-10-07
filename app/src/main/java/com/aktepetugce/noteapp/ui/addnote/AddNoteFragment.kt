package com.aktepetugce.noteapp.ui.addnote

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.aktepetugce.noteapp.NoteApplication
import com.aktepetugce.noteapp.databinding.FragmentAddNoteBinding
import com.aktepetugce.noteapp.util.BitmapResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private var selectedImageBitMap: Bitmap? = null

    private val viewModel: AddNoteViewModel by viewModels() {
        AddNoteViewModelFactory((activity?.application as NoteApplication).addNote)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it.isLoading
            if(!it.errorShown){
                Snackbar.make(requireView(),it.error,Snackbar.LENGTH_LONG).show()
                viewModel.resetError()
            }
            if (it.image != null) {
                binding.imageViewContent.setImageBitmap(it.image)
            }
            if(it.success){
                findNavController().popBackStack()
            }
        }
        initUi()
    }

    private fun initUi() {
        binding.buttonAddNote.setOnClickListener {
            if (selectedImageBitMap == null) {
                selectedImageBitMap = binding.imageViewContent.drawable.toBitmap()
            }
            viewModel.addNote(
                title = binding.editTextTitle.text.toString(),
                content = binding.editTextContent.text.toString(),
                image = selectedImageBitMap!!
            )
        }
        binding.imageViewContent.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityLauncher.launch(intent)
        }
    }

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data!!
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        selectedImageBitMap = BitmapResolver.getBitmap(
                            requireContext().contentResolver,
                            selectedImageUri
                        )
                        if (selectedImageBitMap == null) {
                            selectedImageBitMap = binding.imageViewContent.drawable.toBitmap()
                        }
                        viewModel.saveSelectedPhoto(selectedImageBitMap!!)
                    }

                }

            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}