package com.manish.shopnow.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.manish.shopnow.model.User
import com.manish.shopnow.util.Constants.USER_COLLECTION
import com.manish.shopnow.util.RegisterFieldsState
import com.manish.shopnow.util.RegisterValidation
import com.manish.shopnow.util.Resource
import com.manish.shopnow.util.validateEmail
import com.manish.shopnow.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    // register is a mutable state flow that emits Resource objects containing information about the registration process i.e., success, error, or loading state
    // This can be accessed in a fragment to observe the data
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register // public one to be accessed in a fragment

    private val _validation = Channel<RegisterFieldsState>() // Unlike mutable state flow, Channel does not take any initial value
    val validation = _validation.receiveAsFlow()

    // Creating account with email and password using firebase authentication instance
    fun createAccountWithEmailAndPassword(user: User, password: String) {
        if (checkValidation(user, password)) {
                runBlocking {
                    _register.emit(Resource.Loading())
                }
                firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                    .addOnSuccessListener { authResult ->     // This will get executed if registration was successful
                        authResult.user?.let {
                            saveUserInfo(it.uid, user)
//                            _register.value = Resource.Success(it)
                        }
                    }
                    .addOnFailureListener {       // This will get executed if registration failed
                        _register.value = Resource.Error(it.message.toString())
                    }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateEmail(user.email),
                validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val canRegister =
            (emailValidation is RegisterValidation.Success) && (passwordValidation is RegisterValidation.Success)
        return canRegister
    }
}