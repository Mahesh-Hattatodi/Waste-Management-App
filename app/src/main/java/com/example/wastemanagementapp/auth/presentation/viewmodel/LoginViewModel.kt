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
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.core.util.SnackBarController
import com.example.wastemanagementapp.core.util.SnackBarEvent
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

    val authState : StateFlow<FirebaseUser?> get() = loginRepository.authState

    var email by mutableStateOf("")
        private set

    var isLoginEnabled by mutableStateOf(false)
        private set

    var password by mutableStateOf("")
        private set

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

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
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "Login successful"
                        )
                    )
                    sendEvent(NavigationEvent.Navigate(Screen.HomeScreen))
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
                email = event.email
            }

            is LoginEvent.OnPasswordChange -> {
                password = event.password
            }

            LoginEvent.OnSignInClick -> {
                viewModelScope.launch {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        emailVerifiedOrNot(email).let { verified ->
                            if (verified == true) {
                                loginUserWithEmailAndPassword(email, password)

                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        message = "Login successful"
                                    )
                                )
                                sendEvent(NavigationEvent.Navigate(Screen.HomeScreen))
                            } else {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        message = "Email is not verified"
                                    )
                                )
                                return@launch
                            }
                        }
                    }
                }
            }

            LoginEvent.OnSignUpClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.SignUpScreen))
            }
        }
    }

    private fun sendEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }

    private fun loginUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.loginUserWithEmailAndPassword(email, password)
        }
    }

    private suspend fun emailVerifiedOrNot(email: String): Boolean? {
        return loginRepository.emailVerifiedOrNot(email)
    }

    fun changeLoginButtonState() {
        isLoginEnabled = email.isNotEmpty() && password.isNotEmpty()
    }
}