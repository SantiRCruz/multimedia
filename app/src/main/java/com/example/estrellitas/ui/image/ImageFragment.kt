package com.example.estrellitas.ui.image

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
import com.example.estrellitas.databinding.FragmentImageBinding
import com.example.estrellitas.presentation.ImageViewModel
import com.example.estrellitas.presentation.ImageViewModelFactory
import kotlinx.coroutines.flow.collect

class ImageFragment : Fragment(R.layout.fragment_image) {
    private lateinit var binding: FragmentImageBinding
    private val viewModel by viewModels<ImageViewModel> {
        ImageViewModelFactory(
            AppDatabase.getImageDatabase(
                requireContext()
            ).ImageDao()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImageBinding.bind(view)

        obtainImages()
        clicks()

    }

    private fun clicks() {
        binding.addImage.setOnClickListener {findNavController().navigate(R.id.action_imageFragment_to_createImageFragment)  }
    }

    private fun obtainImages() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.fetchImages().collect {
                when(it){
                    is Result.Success->{
                        val adapter = ImageAdapter(it.data,viewModel)
                        binding.rvImages.adapter = adapter
                        binding.rvImages.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                    }
                }
            }
        }

    }
}