package com.example.purplebox.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.purplebox.R
import com.example.purplebox.activities.AdminActivity
import com.example.purplebox.databinding.FragmentAdminHomeBinding
import com.example.purplebox.viewmodel.admin.AdminViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminHomeFragment : Fragment() {
    val TAG = "AdminHomeFragment"
    private lateinit var viewModel: AdminViewModel
    private lateinit var binding: FragmentAdminHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as AdminActivity).viewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val adminBottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.admin_bottom_navigation)
        adminBottomNavigation.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}