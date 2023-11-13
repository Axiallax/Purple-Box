package com.example.purplebox.viewmodel.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.purplebox.firebaseDatabase.FirebaseDb

class ShoppingViewModelProviderFactory(
    val db: FirebaseDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShoppingViewModel(db) as T
    }
}