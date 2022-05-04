package com.example.estrellitas.ui.image

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.estrellitas.R
import com.example.estrellitas.data.models.ImageEntity
import com.example.estrellitas.databinding.ItemImageBinding
import com.example.estrellitas.presentation.ImageViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.estrellitas.core.Result

class ImageAdapter(private val images:List<ImageEntity>,private val viewModel : ImageViewModel): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemBinding = ItemImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageViewHolder(itemBinding,viewModel)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int  = images.size

    inner class ImageViewHolder(private val binding : ItemImageBinding,private val viewModel : ImageViewModel): RecyclerView.ViewHolder(binding.root){
        fun bind(image : ImageEntity){
            binding.txtTitle.text = image.title
            Log.e("bind: ",image.image )
            binding.imgBackGround.setImageURI(image.image.toUri())
            binding.imgDelete.setOnClickListener {
                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    viewModel.deleteImage(image).collect {
                        when(it){
                            is Result.Loading->{}
                            is Result.Success->{
                                Snackbar.make(
                                    binding.root,
                                    "Eliminado correctamente",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                Navigation.findNavController(binding.root).navigate(R.id.action_imageFragment_self)
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