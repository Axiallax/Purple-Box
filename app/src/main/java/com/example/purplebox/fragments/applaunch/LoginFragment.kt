package com.example.purplebox.fragments.applaunch

import android.content.Intent
//import android.graphics.Color
//import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.purplebox.R
import com.example.purplebox.activities.AdminActivity
import com.example.purplebox.activities.FirstActivity
import com.example.purplebox.activities.ShoppingActivity
import com.example.purplebox.databinding.FragmentLoginBinding
import com.example.purplebox.resource.Resource
import com.example.purplebox.viewmodel.launchapp.PurpleboxViewModel
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {
    val TAG: String = "LoginFragment"
    //val GOOGLE_REQ_CODE = 13
    private lateinit var binding: FragmentLoginBinding
    private lateinit var btnLogin: CircularProgressButton
    private lateinit var viewModel: PurpleboxViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as FirstActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin = view.findViewById(R.id.btn_login_fragment)

        onLoginClick()
        observerLogin()
        observerLoginError()
        onDontHaveAccountClick()
        //onForgotPasswordClick()
        //observeResetPassword()
        }

    /*private fun observeResetPassword() {
        viewModel.resetPassword.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {

                    return@Observer
                }

                is Resource.Success -> {
                    showSnackBar()
                    viewModel.resetPassword.postValue(null)
                    return@Observer
                }

                is Resource.Error -> {
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())

                    return@Observer
                }
            }
        })
    }*/

    /*private fun showSnackBar() {
        Snackbar.make(requireView(),resources.getText(R.string.g_password_reset),Snackbar.LENGTH_LONG).show()
    }*/

    /*private fun onForgotPasswordClick() {
        binding.tvForgotPassword.setOnClickListener {
            setupBottomSheetDialog()
        }
    }*/

    /*private fun setupBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        val view = layoutInflater.inflate(R.layout.forgot_password_dialog, null)
        dialog.setContentView(view)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()

        val edEmail = view.findViewById<EditText>(R.id.ed_email_register)
        val btnSend = view.findViewById<Button>(R.id.btn_send)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

        btnSend.setOnClickListener {
            val email = edEmail.text.toString().trim()
            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                viewModel.resetPassword(email)
                dialog.dismiss()
            } else {
                edEmail.requestFocus()
                edEmail.error = resources.getText(R.string.g_check_your_email)
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }*/

    private fun onDontHaveAccountClick() {
        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun observerLoginError() {
        viewModel.loginError.observe(viewLifecycleOwner, Observer { error ->
            Log.e(TAG, error)
            Toast.makeText(activity, "Please check your information", Toast.LENGTH_LONG).show()
            btnLogin.revertAnimation()

        })


    }

    private fun observerLogin() {
        viewModel.login.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                btnLogin.revertAnimation()
                val intent = Intent(activity, AdminActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }


    private fun onLoginClick() {
        btnLogin.setOnClickListener {
            btnLogin.spinningBarWidth = resources.getDimension(com.intuit.sdp.R.dimen._3sdp)

            val email = getEmail()?.trim()
            val password = getPassword()
            email?.let {
                password?.let {
                    btnLogin.startAnimation()
                    viewModel.loginUser(email, password)
                }
            }
        }
    }
    private fun getPassword(): String? {
        val password = binding.edPasswordLogin.text.toString()

        if (password.isEmpty()) {
            binding.edPasswordLogin.apply {
                error = resources.getString(R.string.password_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (password.length < 6) {
            binding.edPasswordLogin.apply {
                error = resources.getString(R.string.password_at_least_six)
                requestFocus()
            }
            return null
        }
        return password
    }

    private fun getEmail(): String? {
        val email = binding.edEmailLogin.text.toString().trim()

        if (email.isEmpty()) {
            binding.edEmailLogin.apply {
                error = resources.getString(R.string.email_cant_be_empty)
                requestFocus()
            }
            return null
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmailLogin.apply {
                error = resources.getString(R.string.valid_email)
                requestFocus()
            }
            return null
        }


        return email

    }
}