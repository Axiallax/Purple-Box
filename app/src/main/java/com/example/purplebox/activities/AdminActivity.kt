package com.example.purplebox.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.purplebox.R
import com.example.purplebox.firebaseDatabase.FirebaseDb
import com.example.purplebox.viewmodel.admin.AdminViewModel
import com.example.purplebox.viewmodel.admin.AdminViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {

    val viewModel by lazy {
        val aFDatabase = FirebaseDb()
        val adminProviderFactory = AdminViewModelProviderFactory(aFDatabase)
        ViewModelProvider(this, adminProviderFactory)[AdminViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val adminBottomNavigation = findViewById<BottomNavigationView>(R.id.admin_bottom_navigation)
        val adminNavController = Navigation.findNavController(this, R.id.admin_host_fragment)
        NavigationUI.setupWithNavController(adminBottomNavigation, adminNavController)
    }
}
