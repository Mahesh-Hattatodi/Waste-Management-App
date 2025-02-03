package com.example.wastemanagementapp.auth.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.wastemanagementapp.auth.presentation.events.LoginEvent
import com.example.wastemanagementapp.auth.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun LoginScreenContainer(
    viewModel : LoginViewModel,
    context: Context,
    scope : CoroutineScope
) {
    val googleSignInClient = GoogleSignInClient(context)

    LoginScreen(
        onGoogleSignInClick = {
            scope.launch {
                val authResult = googleSignInClient.googleSignIn()

                viewModel.saveGoogleUser(authResult)
            }
        },
        onEvent = viewModel::onEvent
    )
}

@Composable
fun LoginScreen(
    onGoogleSignInClick : () -> Unit = {},
    onEvent : (LoginEvent) -> Unit = {}
) {

}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}