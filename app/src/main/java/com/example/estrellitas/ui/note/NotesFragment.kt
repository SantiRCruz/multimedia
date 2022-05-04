package com.example.estrellitas.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.estrellitas.R
import com.example.estrellitas.core.Result
import com.example.estrellitas.data.local.AppDatabase
import com.example.estrellitas.databinding.FragmentNotesBinding
import com.example.estrellitas.presentation.ImageViewModel
import com.example.estrellitas.presentation.ImageViewModelFactory
import com.example.estrellitas.presentation.NoteViewModel
import com.example.estrellitas.presentation.NoteViewModelFactory
import com.example.estrellitas.ui.image.ImageAdapter


class NotesFragment : Fragment(R.layout.fragment_notes) {
    private lateinit var binding : FragmentNotesBinding
    private val viewModel by viewModels<NoteViewModel> {
        NoteViewModelFactory(
            AppDatabase.getNoteDatabase(
                requireContext()
            ).NoteDao()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotesBinding.bind(view)

        obtainNotes()
        clicks()

    }
    private fun clicks() {
        binding.addNote.setOnClickListener {findNavController().navigate(R.id.action_notesFragment_to_createNoteFragment)  }
    }

    private fun obtainNotes() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.fetchNotes().collect {
                when(it){
                    is Result.Success->{
                        val adapter = NoteAdapter(it.data)
                        binding.rvNotes.adapter = adapter
                        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(2,
                            StaggeredGridLayoutManager.VERTICAL)
                    }
                }
            }
        }
    }
}