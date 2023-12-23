package com.example.purplebox.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.purplebox.R
import com.example.purplebox.firebaseDatabase.FirebaseDb
import com.example.purplebox.fragments.admin.AdminHomeFragment
import com.example.purplebox.viewmodel.admin.AdminViewModel
import com.example.purplebox.viewmodel.admin.AdminViewModelProviderFactory

class AdminActivity : AppCompatActivity() {

    val viewModel by lazy {
        val fDatabase = FirebaseDb()
        val providerFactory = AdminViewModelProviderFactory(fDatabase)
        ViewModelProvider(this, providerFactory)[AdminViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
    }
}
