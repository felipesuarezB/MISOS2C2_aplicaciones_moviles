package com.example.viniloapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.viniloapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        binding.navView.setupWithNavController(navController)


        val bottomNav: BottomNavigationView = binding.navView
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.navigation_collectors -> {
                    navController.popBackStack(R.id.navigation_collectors, false)
                }
                R.id.navigation_albums -> {
                    navController.popBackStack(R.id.navigation_albums, false)
                }
                R.id.navigation_artists -> {
                    navController.popBackStack(R.id.navigation_artists, false)
                }
            }
        }

    }
}