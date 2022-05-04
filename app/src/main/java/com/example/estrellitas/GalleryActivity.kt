package com.example.estrellitas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.estrellitas.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGalleryBinding

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        observeDestination()

    }

    private fun observeDestination() {
        navController.addOnDestinationChangedListener {n,d,a->
            when(d.id){
                R.id.createImageFragment ->{binding.bottomNavigationView.visibility = View.GONE}
                R.id.createMusicFragment ->{binding.bottomNavigationView.visibility = View.GONE}
                R.id.createNoteFragment ->{binding.bottomNavigationView.visibility = View.GONE}
                R.id.updateFragment ->{binding.bottomNavigationView.visibility = View.GONE}
                R.id.imageFragment ->{binding.bottomNavigationView.visibility = View.VISIBLE}
                R.id.musicFragment ->{binding.bottomNavigationView.visibility = View.VISIBLE}
                R.id.notesFragment ->{binding.bottomNavigationView.visibility = View.VISIBLE}
            }
        }
    }
}