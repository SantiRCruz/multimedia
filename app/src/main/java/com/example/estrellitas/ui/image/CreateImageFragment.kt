package com.example.estrellitas.ui.image

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.estrellitas.R
import com.example.estrellitas.core.Result
import com.example.estrellitas.data.local.AppDatabase
import com.example.estrellitas.data.models.ImageEntity
import com.example.estrellitas.databinding.FragmentCreateImageBinding
import com.example.estrellitas.presentation.ImageViewModel
import com.example.estrellitas.presentation.ImageViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.io.File


class CreateImageFragment : Fragment(R.layout.fragment_create_image) {
    private lateinit var binding: FragmentCreateImageBinding
    private val viewModel by viewModels<ImageViewModel> {
        ImageViewModelFactory(
            AppDatabase.getImageDatabase(
                requireContext()
            ).ImageDao()
        )
    }
    private var uriResult: Uri? = null
    private lateinit var root: String


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickPhotoFromGallery()
            } else {
                Snackbar.make(
                    binding.root,
                    "You need to enable the permission",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.data
                requireActivity().contentResolver.takePersistableUriPermission(
                    data!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                uriResult = data
                binding.imgBackGround.setImageURI(data)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateImageBinding.bind(view)
        clicks()
    }

    private fun clicks() {
        binding.imgBack.setOnClickListener { findNavController().popBackStack() }
        binding.imgUpload.setOnClickListener { requestPermission() }
        binding.btnSave.setOnClickListener { saveImage() }
    }

    private fun saveImage() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveImage(
                    ImageEntity(
                        0,
                        uriResult.toString(),
                        binding.titleEdt.text.toString()
                    )
                ).collect {
                    when (it) {
                        is Result.Success -> {
                            Snackbar.make(binding.root, "saved correctly", Snackbar.LENGTH_SHORT)
                                .show()
                            findNavController().popBackStack()
                        }

                    }
                }
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickPhotoFromGallery()
                }
                else -> {
                    requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }
        } else {
            pickPhotoFromGallery()
        }
    }

    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        galleryResult.launch(intent)
    }
}