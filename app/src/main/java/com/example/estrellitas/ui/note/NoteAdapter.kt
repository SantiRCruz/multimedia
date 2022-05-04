package com.example.estrellitas.ui.note

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.estrellitas.R
import com.example.estrellitas.core.Result
import com.example.estrellitas.data.models.ImageEntity
import com.example.estrellitas.data.models.NoteEntity
import com.example.estrellitas.databinding.ItemImageBinding
import com.example.estrellitas.databinding.ItemNotesBinding
import com.example.estrellitas.presentation.ImageViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteAdapter(private val notes:List<NoteEntity>): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemBinding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int  = notes.size

    inner class NoteViewHolder(private val binding : ItemNotesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(note : NoteEntity){
            binding.txtTitle.text = note.title
            binding.txtDescription.text = note.description
            binding.root.setOnClickListener {
                val action = NotesFragmentDirections.actionNotesFragmentToUpdateFragment(note)
                Navigation.findNavController(binding.root).navigate(action) }
        }
    }
}