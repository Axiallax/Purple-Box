package com.example.purplebox.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.purplebox.R
import com.example.purplebox.firebaseDatabase.FirebaseDb
import com.example.purplebox.viewmodel.shopping.ShoppingViewModel
import com.example.purplebox.viewmodel.shopping.ShoppingViewModelProviderFactory
import com.example.purplebox.viewmodel.shopping.cart.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShoppingActivity : AppCompatActivity() {

    val viewModel by lazy {
        val fDatabase = FirebaseDb()
        val providerFactory = ShoppingViewModelProviderFactory(fDatabase)
        ViewModelProvider(this, providerFactory)[ShoppingViewModel::class.java]
    }

    private lateinit var cartViewModel: CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)



        cartViewModel = CartViewModel()
        supportActionBar!!.hide()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)

//        observeCartProductsCount(bottomNavigation)
    }
}