package com.example.estrellitas.ui.music

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.estrellitas.R
import com.example.estrellitas.core.Result
import com.example.estrellitas.data.models.MusicEntity
import com.example.estrellitas.databinding.ItemAudioBinding
import com.example.estrellitas.presentation.MusicViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MusicAdapter(private val musics:List<MusicEntity>,private val viewModel : MusicViewModel): RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val itemBinding = ItemAudioBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MusicViewHolder(itemBinding,parent.context,viewModel)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(musics[position])
    }

    override fun getItemCount(): Int = musics.size

    inner class MusicViewHolder(private val binding : ItemAudioBinding,private val context: Context,private val viewModel : MusicViewModel):
        RecyclerView.ViewHolder(binding.root){
        fun bind(music : MusicEntity){
            val mediaPlayer  = MediaPlayer()
            var firstTimeSound = 0
            binding.txtTitle.text = music.title
            binding.imgPlay.setOnClickListener {
                binding.imgPlay.visibility = View.GONE
                binding.imgStop.visibility = View.VISIBLE
                if (firstTimeSound == 0) {
                    mediaPlayer.setDataSource(context, music.audio.toUri())
                    firstTimeSound = 1
                }
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
            binding.imgStop.setOnClickListener {
                binding.imgPlay.visibility = View.VISIBLE
                binding.imgStop.visibility = View.GONE
                mediaPlayer.stop()
            }
            binding.imgDelete.setOnClickListener {
                val scope  = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    viewModel.deleteMusic(music).collect {
                        when(it){
                            is Result.Success->{
                                Snackbar.make(
                                    binding.root,
                                    "Eliminado correctamente",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                Navigation.findNavController(binding.root).navigate(R.id.action_musicFragment_self)
                            }
                            is Result.Failure->{
                                Snackbar.make(
                                    binding.root,
                                    "Error al eliminar los datos",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                Log.e("Error", "bind: ${it.exception}", )
                            }
                        }
                    }
                }
            }
        }
    }
}