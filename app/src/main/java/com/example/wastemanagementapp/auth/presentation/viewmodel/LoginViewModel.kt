package com.example.wastemanagementapp.auth.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.auth.domain.LoginRepository
import com.example.wastemanagementapp.auth.presentation.events.LoginEvent
import com.example.wastemanagementapp.core.domain.UserProfile
import com.example.wastemanagementapp.core.util.SnackBarController
import com.example.wastemanagementapp.core.util.SnackBarEvent
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("NAME_SHADOWING")
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile = _userProfile.asStateFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun saveGoogleUser(authResult: Result<AuthResult>) {
        authResult.fold(
            onSuccess = { authResult ->
                _userProfile.update { userProfile ->
                    userProfile.copy(
                        uuid = authResult.user!!.uid,
                        displayName = authResult.user!!.displayName,
                        email = authResult.user!!.email,
                        photoUrl = authResult.user!!.photoUrl.toString()
                    )
                }

                viewModelScope.launch {
                    saveUserProfile(_userProfile.value)
                }
            },
            onFailure = {
                viewModelScope.launch {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "Login unsuccessful"
                        )
                    )
                }

                Log.i("login", "saveUser: Login unsuccessful $it")
            }
        )
    }

    private suspend fun saveUserProfile(userProfile: UserProfile) {
        withContext(Dispatchers.IO) {
            loginRepository.saveGoogleUserProfile(userProfile)
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> {
                email += event.email
            }
            is LoginEvent.OnPasswordChange -> {
                password += event.password
            }
            LoginEvent.OnSignInClick -> TODO()
            LoginEvent.OnSignUpClick -> TODO()
        }
    }
}