package com.example.wastemanagementapp.auth.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.auth.domain.SignUpRepository
import com.example.wastemanagementapp.auth.domain.use_cases.ValidateConfirmPassword
import com.example.wastemanagementapp.auth.domain.use_cases.ValidateEmail
import com.example.wastemanagementapp.auth.domain.use_cases.ValidatePassword
import com.example.wastemanagementapp.auth.presentation.state.RegistrationFormState
import com.example.wastemanagementapp.auth.presentation.events.SignUpEvent
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.core.util.SnackBarController
import com.example.wastemanagementapp.core.util.SnackBarEvent
import com.example.wastemanagementapp.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateConfirmPassword: ValidateConfirmPassword,
    private val repository: SignUpRepository
) : ViewModel() {
    var state by mutableStateOf(RegistrationFormState())

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnConfirmPasswordChange -> {
                state = state.copy(confirmPassword = event.confirmPassword)
            }
            is SignUpEvent.OnEmailChange -> {
                state = state.copy(email = event.email)
            }
            is SignUpEvent.OnNameChange -> {
                state = state.copy(name = event.name)
            }
            is SignUpEvent.OnPasswordChange -> {
                state = state.copy(password = event.password)
            }
            is SignUpEvent.OnWardChange -> {
               state = state.copy(ward = event.ward)
            }
            SignUpEvent.OnSignUpClick -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val confirmPasswordResult = validateConfirmPassword.execute(state.password, state.confirmPassword)

        val hasError = listOf(
            emailResult,
            passwordResult,
            confirmPasswordResult
        ).any { !it.success }

        if (hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                confirmPasswordError = confirmPasswordResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            saveUserAndSendEmailVerification(
                email = state.email,
                password = state.password,
                displayName = state.name,
                ward = state.ward
            )

            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = UiText.StringResource(
                        resId = R.string.verification_email_is_sent
                    )
                )
            )
            sendEvent(NavigationEvent.Navigate(Screen.LoginScreen))
        }
    }

    private suspend fun saveUserAndSendEmailVerification(email: String, password: String, displayName: String, ward: String) {
        withContext(Dispatchers.IO) {
            repository.saveUserAndSendEmailVerification(
                email = email,
                password = password,
                displayName = displayName,
                ward = ward
            )
        }
    }

    private fun sendEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}