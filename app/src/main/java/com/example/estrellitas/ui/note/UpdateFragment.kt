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
import androidx.navigation.fragment.navArgs
import com.example.estrellitas.R
import com.example.estrellitas.core.Result
import com.example.estrellitas.data.local.AppDatabase
import com.example.estrellitas.data.models.NoteEntity
import com.example.estrellitas.databinding.FragmentUpdateBinding
import com.example.estrellitas.presentation.NoteViewModel
import com.example.estrellitas.presentation.NoteViewModelFactory
import com.google.android.material.snackbar.Snackbar

class UpdateFragment : Fragment(R.layout.fragment_update) {
    private lateinit var binding: FragmentUpdateBinding
    private val viewModel by viewModels<NoteViewModel> {
        NoteViewModelFactory(
            AppDatabase.getNoteDatabase(
                requireContext()
            ).NoteDao()
        )
    }
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateBinding.bind(view)
        setData()
        clicks()
    }

    private fun setData() {
        binding.edtTitle.setText(args.note.title)
        binding.edtDescription.setText(args.note.description)
    }

    private fun clicks() {
        binding.imgBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnUpdate.setOnClickListener { validate() }
        binding.imgDelete.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.deleteNote(args.note).collect {
                        when(it){
                            is Result.Success ->{
                                Snackbar.make(binding.root,"deleted correctly", Snackbar.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                            is Result.Failure ->{
                                Snackbar.make(binding.root,"${it.exception}", Snackbar.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validate() {
        val result = arrayOf(validateTitle(),validateDescription())
        if (false in result){
            return
        }
        updateImage()
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
    private fun updateImage() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.updateNote(NoteEntity(args.note.id,binding.edtTitle.text.toString(),binding.edtDescription.text.toString())).collect {
                    when(it){
                        is Result.Success ->{
                            Snackbar.make(binding.root,"updated correctly", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        is Result.Failure ->{
                            Snackbar.make(binding.root,"${it.exception}", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }
}