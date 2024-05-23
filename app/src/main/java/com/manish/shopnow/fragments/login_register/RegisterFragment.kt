package com.manish.shopnow.fragments.login_register

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.manish.shopnow.R
import com.manish.shopnow.databinding.FragmentRegisterBinding
import com.manish.shopnow.model.User
import com.manish.shopnow.util.RegisterValidation
import com.manish.shopnow.util.Resource
import com.manish.shopnow.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment: Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        binding.tvDoYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        // trim function trims the spaces at tha beginning and at the end
        binding.apply {
            buttonRegisterRegister.setOnClickListener {
                val user = User(
                    etFirstNameRegister.text.toString().trim(),
                    etLastNameRegister.text.toString().trim(),
                    etEmailRegister.text.toString().trim()
                )
                val password = etPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }
        // Observing the register operation
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {// Here we have Resource of Firebase user
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test", it.data.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                        Toast.makeText(requireContext(), "Registration successful, login now", Toast.LENGTH_LONG).show()
                        // Navigate to login fragment upon successful registration
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                    } else -> Unit // Unit means nothing
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        // This will show error in the edit text
                        binding.etEmailRegister.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        // This will show error in the edit text
                        binding.etPasswordRegister.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
}