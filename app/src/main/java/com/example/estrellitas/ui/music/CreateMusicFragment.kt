package com.example.estrellitas.ui.music

import android.app.Activity
import android.content.Intent
import android.content.UriPermission
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.estrellitas.R
import com.example.estrellitas.data.local.AppDatabase
import com.example.estrellitas.data.models.MusicEntity
import com.example.estrellitas.databinding.FragmentCreateMusicBinding
import com.example.estrellitas.presentation.ImageViewModel
import com.example.estrellitas.presentation.ImageViewModelFactory
import com.example.estrellitas.presentation.MusicViewModel
import com.example.estrellitas.presentation.MusicViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.example.estrellitas.core.Result


class CreateMusicFragment : Fragment(R.layout.fragment_create_music) {
    private lateinit var binding : FragmentCreateMusicBinding
    private  var mediaPlayer: MediaPlayer? =null

    private val viewModel by viewModels<MusicViewModel> {
        MusicViewModelFactory(
            AppDatabase.getMusicDatabase(
                requireContext()
            ).MusicDao()
        )
    }
    private var firstTimeSound = 0
    private var uriResult: Uri? = null
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickAudioFromGallery()
            } else {
                Snackbar.make(
                    binding.root,
                    "You need to enable the permission",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    private val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                mediaPlayer = MediaPlayer()
                firstTimeSound = 0
                binding.imgPlay.visibility = View.VISIBLE
                binding.imgStop.visibility = View.GONE
                val data = it.data?.data
                uriResult = null
                uriResult = data
                requireActivity().contentResolver.takePersistableUriPermission(data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateMusicBinding.bind(view)

        clicks()
    }

    private fun clicks() {
        binding.imgBack.setOnClickListener {
            if (mediaPlayer != null){
                mediaPlayer!!.stop()
            }
            findNavController().popBackStack()
        }
        binding.imgPlay.setOnClickListener {
            if (firstTimeSound == 0) {
                mediaPlayer?.setDataSource(requireContext(), uriResult!!)
                firstTimeSound = 1
            }
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            binding.imgPlay.visibility = View.GONE
            binding.imgStop.visibility = View.VISIBLE
        }
        binding.imgStop.setOnClickListener {
            mediaPlayer?.stop()
            binding.imgPlay.visibility = View.VISIBLE
            binding.imgStop.visibility = View.GONE
        }
        binding.imgUpload.setOnClickListener { requestPermission() }
        binding.btnSave.setOnClickListener { saveAudio() }

    }

    private fun saveAudio() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.saveMusic(MusicEntity(0,uriResult.toString(),binding.titleEdt.text.toString())).collect {
                    when(it){
                        is Result.Success ->{
                            Snackbar.make(binding.root,"Save Correctly",Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        is Result.Failure ->{
                            Snackbar.make(binding.root,it.toString(),Snackbar.LENGTH_SHORT).show()
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
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickAudioFromGallery()
                }
                else -> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun pickAudioFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "audio/*"
        galleryResult.launch(intent)
    }
}