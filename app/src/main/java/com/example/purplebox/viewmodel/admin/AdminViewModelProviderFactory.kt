package com.example.purplebox.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.purplebox.firebaseDatabase.FirebaseDb
import com.example.purplebox.viewmodel.shopping.ShoppingViewModel

class AdminViewModelProviderFactory(
    val adb: FirebaseDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdminViewModel(adb) as T
    }
}