package com.example.purplebox.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.purplebox.firebaseDatabase.FirebaseDb
import com.example.purplebox.viewmodel.launchapp.PurpleboxViewModel

class AdminViewModelProviderFactory(
    private val firebaseDb: FirebaseDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PurpleboxViewModel(firebaseDb) as T
    }
}