package com.example.purplebox.viewmodel.admin

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.purplebox.firebaseDatabase.FirebaseDb

class AdminViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {
    val selectedColors = mutableListOf<Int>()
    var selectedImages = mutableListOf<Uri>()


/*    fun saveProduct(product, product) {
        firebaseDatabase.productRef(product)
    }*/
}