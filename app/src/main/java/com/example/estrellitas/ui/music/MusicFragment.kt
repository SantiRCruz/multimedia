package com.example.estrellitas.ui.music

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estrellitas.R
import com.example.estrellitas.data.local.AppDatabase
import com.example.estrellitas.databinding.FragmentMusicBinding
import com.example.estrellitas.presentation.MusicViewModel
import com.example.estrellitas.presentation.MusicViewModelFactory
import com.example.estrellitas.core.Result


class MusicFragment : Fragment(R.layout.fragment_music) {
    private lateinit var binding : FragmentMusicBinding
    private val viewModel by viewModels<MusicViewModel> {
        MusicViewModelFactory(
            AppDatabase.getMusicDatabase(
                requireContext()
            ).MusicDao()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMusicBinding.bind(view)
        obtainMusic()
        clicks()
    }
    private fun clicks() {
        binding.addMusic.setOnClickListener { findNavController().navigate(R.id.action_musicFragment_to_createMusicFragment) }
    }
    private fun obtainMusic() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.fetchMusic().collect {
                    when(it){
                        is Result.Loading->{}
                        is Result.Success->{
                            val adapter = MusicAdapter(it.data,viewModel)
                            binding.rvMusic.layoutManager = LinearLayoutManager(requireContext())
                            binding.rvMusic.adapter=adapter
                        }
                        is Result.Failure->{}
                    }
                }
            }
        }
    }
}