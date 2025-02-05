package com.example.wastemanagementapp.auth.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.auth.presentation.events.LoginEvent
import com.example.wastemanagementapp.auth.presentation.viewmodel.LoginViewModel
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.ObserveAsEvents
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.ui.theme.Black10
import com.example.wastemanagementapp.ui.theme.Black20
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val textFieldShadow = Modifier
    .shadow(
        elevation = 8.dp,
        spotColor = Black10,
        shape = RoundedCornerShape(25.dp)
    )

@Composable
fun LoginScreenContainer(
    viewModel: LoginViewModel = hiltViewModel(),
    context: Context,
    scope: CoroutineScope,
    onNavigate: (NavigationEvent.Navigate) -> Unit
) {
    val googleSignInClient = GoogleSignInClient(context)

    ObserveAsEvents(flow = viewModel.navigationEvent) { event ->
        when (event) {
            is NavigationEvent.Navigate -> {
                onNavigate(event)
            }
            NavigationEvent.PopBackStack -> Unit
        }
    }


    LoginScreen(
        onGoogleSignInClick = {
            scope.launch {
                val authResult = googleSignInClient.googleSignIn()

                viewModel.saveGoogleUser(authResult)
            }
        },
        onEvent = viewModel::onEvent,
        email = viewModel.email,
        password = viewModel.password
    )
}

@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit = {},
    onEvent: (LoginEvent) -> Unit = {},
    email: String = "",
    password: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.truck_delivery_service),
            contentDescription = stringResource(
                R.string.truck_delivery_service_image
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(start = 64.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.login),
                color = MaterialTheme.colorScheme.secondaryContainer,
                fontSize = 64.sp,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.please_sign_in_to_continue),
                color = Black20,
                fontSize = 20.sp,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = email,
                onValueChange = {
                    onEvent(LoginEvent.OnEmailChange(it))
                },
                modifier = textFieldShadow,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(R.string.enter_email)
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.e_mail),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    focusedTextColor = Black20,
                    unfocusedTextColor = Black20
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            TextField(
                value = password,
                onValueChange = {
                    onEvent(LoginEvent.OnPasswordChange(it))
                },
                modifier = textFieldShadow,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.enter_password)
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.password),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                singleLine = true,
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    focusedTextColor = Black20,
                    unfocusedTextColor = Black20
                )
            )
        }

        Button(
            onClick = { onEvent(LoginEvent.OnSignInClick) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = stringResource(R.string.login),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.or),
            color = Black20
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = onGoogleSignInClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = Color.White
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_google),
                contentDescription = stringResource(R.string.google_icon)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.sign_in_with_google)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dont_t_have_an_account),
                color = Black20
            )

            TextButton(onClick = { onEvent(LoginEvent.OnSignUpClick) }) {
                Text(
                    text = stringResource(R.string.sign_up),
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}