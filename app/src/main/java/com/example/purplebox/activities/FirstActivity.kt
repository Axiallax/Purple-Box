package com.example.purplebox.activities

import  android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.purplebox.R
import com.example.purplebox.firebaseDatabase.FirebaseDb
import com.example.purplebox.viewmodel.launchapp.PurpleboxViewModel
import com.example.purplebox.viewmodel.launchapp.ViewModelProviderFactory

class FirstActivity : AppCompatActivity() {
    val viewModel by lazy {
        val firebaseDb = FirebaseDb()
        val viewModelFactory = ViewModelProviderFactory(firebaseDb)
        ViewModelProvider(this,viewModelFactory)[PurpleboxViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        supportActionBar?.hide()
    }
}