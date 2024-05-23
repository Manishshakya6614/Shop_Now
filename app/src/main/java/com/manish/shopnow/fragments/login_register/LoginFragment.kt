package com.manish.shopnow.fragments.login_register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.manish.shopnow.R
import com.manish.shopnow.activities.ShoppingActivity
import com.manish.shopnow.databinding.FragmentLoginBinding
import com.manish.shopnow.dialog.setupBottomSheetDialog
import com.manish.shopnow.util.Resource
import com.manish.shopnow.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.tvDontHavAnAcc.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            btnLogin.setOnClickListener {
//                val email = etEmailLogin.text.toString().trim()
//                val password = etPasswordLogin.text.toString()
//                viewModel.loginUser(email, password)
                if (etEmailLogin.text.toString().trim().isEmpty()) {
                    binding.etEmailLogin.requestFocus()
                    binding.etEmailLogin.error = "Email cannot be empty"
                } else if (etPasswordLogin.text.toString().trim().isEmpty()) {
                    binding.etPasswordLogin.requestFocus()
                    binding.etPasswordLogin.error = "Password cannot be empty"
                } else {
                    viewModel.loginUser(etEmailLogin.text.toString().trim(), etPasswordLogin.text.trim().toString())
                }
            }
        }

        binding.tvForgotPasswordLogin.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.resetPassword(email)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Snackbar.make(requireView(), "Reset link sent to your email", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), "Error: ${it.message}", Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnLogin.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnLogin.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            // This flag will make sure to pop this activity from the stack, so when the user logins
                            // to their account they will navigate to shopping activity and if they navigate back, we don't need
                            // to go back to LoginRegisterActivity as the user is logged in now
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error -> {
                        binding.btnLogin.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

}