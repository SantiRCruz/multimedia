package com.example.estrellitas.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.estrellitas.R
import com.example.estrellitas.core.Result
import com.example.estrellitas.data.local.AppDatabase
import com.example.estrellitas.data.models.ImageEntity
import com.example.estrellitas.data.models.NoteEntity
import com.example.estrellitas.databinding.FragmentCreateNoteBinding
import com.example.estrellitas.presentation.NoteViewModel
import com.example.estrellitas.presentation.NoteViewModelFactory
import com.google.android.material.snackbar.Snackbar


class CreateNoteFragment : Fragment(R.layout.fragment_create_note) {
    private lateinit var binding : FragmentCreateNoteBinding
    private val viewModel by viewModels<NoteViewModel> {
        NoteViewModelFactory(
            AppDatabase.getNoteDatabase(
                requireContext()
            ).NoteDao()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateNoteBinding.bind(view)
        clicks()
    }

    private fun clicks() {
        binding.imgBack.setOnClickListener {findNavController().popBackStack()}
        binding.btnSave.setOnClickListener {validate()}
    }

    private fun validate() {
        val result = arrayOf(validateTitle(),validateDescription())
        if (false in result){
            return
        }
        saveImage()
    }

    private fun validateTitle(): Boolean {
        return if(binding.edtTitle.text.toString().isNullOrEmpty()){
            binding.edtTitle.error = "No puedes dejar este campo vacio"
            false
        }else{
            true
        }
    }
    private fun validateDescription(): Boolean {
        return if(binding.edtDescription.text.toString().isNullOrEmpty()){
            binding.edtDescription.error = "No puedes dejar este campo vacio"
            false
        }else{
            true
        }
    }

    private fun saveImage() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.saveNote(NoteEntity(0,binding.edtTitle.text.toString(),binding.edtDescription.text.toString())).collect {
                    when(it){
                        is Result.Success ->{
                            Snackbar.make(binding.root,"saved correctly", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }

                    }
                }
            }
        }
    }
}