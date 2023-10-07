package com.aktepetugce.noteapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aktepetugce.noteapp.R
import com.aktepetugce.noteapp.databinding.ItemLayoutNoteBinding
import com.aktepetugce.noteapp.domain.model.Note

class NoteAdapter(
    private val clickNote: (Note) -> Unit,
    private val longClickNote: (Note) -> Unit,
) : ListAdapter<Note, NoteAdapter.NotesViewHolder>(DiffCallback()) {

    inner class NotesViewHolder(private val binding: ItemLayoutNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(note: Note) {
            with(binding) {
                layoutNoteItem.setOnClickListener {
                    clickNote(note)
                }
                layoutNoteItem.setOnLongClickListener {
                    longClickNote(note)
                    return@setOnLongClickListener true
                }
                textViewContent.text = note.content
                textViewTitle.text = note.title
                textViewModifiedDate.text = note.modified
                if (note.isEdited) {
                    textViewIsEdited.text = binding.root.resources.getString(R.string.edited)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemLayoutNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotesViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bindTo(note)
    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}