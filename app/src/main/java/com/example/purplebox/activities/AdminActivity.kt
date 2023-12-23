package com.example.purplebox.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.purplebox.R
import com.example.purplebox.fragments.admin.AdminHomeFragment

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AdminHomeFragment.newInstance())
                .commitNow()
        }
    }
}