package com.example.purplebox.fragments.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.purplebox.R
import com.example.purplebox.activities.FirstActivity
import com.example.purplebox.databinding.FragmentAdminHomeBinding
import com.example.purplebox.databinding.FragmentProfileBinding
import com.example.purplebox.viewmodel.admin.AdminViewModel
import com.google.firebase.auth.FirebaseAuth

class AdminHomeFragment : Fragment() {

    companion object {
        fun newInstance() = AdminHomeFragment()
    }

    private lateinit var binding: FragmentAdminHomeBinding
    private lateinit var viewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    private fun onLogoutClick() {

        binding.linearOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, FirstActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}