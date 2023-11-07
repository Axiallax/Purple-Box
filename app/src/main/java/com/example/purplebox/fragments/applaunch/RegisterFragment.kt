package com.example.purplebox.fragments.applaunch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.purplebox.R
import com.example.purplebox.activities.LoginRegisterActivity
import com.example.purplebox.databinding.FragmentRegisterBinding
import com.example.purplebox.model.User
import com.example.purplebox.resource.Resource
import com.example.purplebox.viewmodel.launchapp.PurpleboxViewModel
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton

private const val TAG = "RegisterFragment"
class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: PurpleboxViewModel
    lateinit var btnRegister: CircularProgressButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as LoginRegisterActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister = view.findViewById(R.id.btn_login_fragment)

        onRegisterBtnClick()
        observeSaveUserInformation()
        onLoginClick()
    }

    private fun onLoginClick() {
        binding.tvHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun onRegisterBtnClick() {
        btnRegister.setOnClickListener {
            btnRegister.spinningBarColor = resources.getColor(R.color.white)
            btnRegister.spinningBarWidth = resources.getDimension(com.intuit.sdp.R.dimen._3sdp)
            val user = getUser()
            val password = getPassword()
            user?.let { user ->
                password?.let { password ->
                    viewModel.registerNewUser(user, password)
                    btnRegister.startAnimation()
                }
            }
        }
    }

    private fun observeSaveUserInformation() {
        viewModel.register.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "EmailRegister:Loading")
                    btnRegister.startAnimation()
                    return@Observer
                }

                is Resource.Success -> {
                    Log.d(TAG, "EmailRegister:Successful")
                    btnRegister.stopAnimation()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.signed_up_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.logOut()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    viewModel.register.postValue(null)
                }

                is Resource.Error -> {
                    Log.e(TAG, "EmailRegister:Error ${response.message.toString()}")
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_LONG
                    ).show()
                    return@Observer
                }
            }
        })
    }

    private fun getUser(): User? {
        val userName = binding.edUsernameRegister.text.toString().trim()
        val contactNumber = binding.edContactRegister.text.toString().trim()
        val email = binding.edEmailRegister.text.toString().trim()

        if (userName.isEmpty()) {
            binding.edUsernameRegister.apply {
                error = resources.getString(R.string.first_name_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (contactNumber.isEmpty()) {
            binding.edContactRegister.apply {
                error = resources.getString(R.string.last_name_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (email.isEmpty()) {
            binding.edEmailRegister.apply {
                error = resources.getString(R.string.email_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmailRegister.apply {
                error = resources.getString(R.string.valid_email)
                requestFocus()
            }
            return null
        }


        return User(userName, contactNumber, email)
    }

    private fun getPassword(): String? {
        val password = binding.edPasswordRegister.text.toString().trim()
        if (password.isEmpty()) {
            binding.edPasswordRegister.apply {
                error = resources.getString(R.string.password_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (password.length < 6) {
            binding.edPasswordRegister.apply {
                error = resources.getString(R.string.password_at_least_six)
                requestFocus()
            }
            return null
        }
        return password
    }

}