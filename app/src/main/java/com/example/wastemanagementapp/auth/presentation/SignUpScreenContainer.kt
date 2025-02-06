package com.example.wastemanagementapp.auth.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.auth.presentation.events.SignUpEvent
import com.example.wastemanagementapp.auth.presentation.viewmodel.SignUpViewModel
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.ObserveAsEvents
import com.example.wastemanagementapp.core.util.UiText
import com.example.wastemanagementapp.ui.theme.Black20

@Composable
fun SignUpScreenContainer(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent.Navigate) -> Unit = {}
) {
    val state = viewModel.state

    ObserveAsEvents(flow = viewModel.navigationEvent) { event ->
        when (event) {
            is NavigationEvent.Navigate -> {
                onNavigate(event)
            }

            NavigationEvent.PopBackStack -> Unit
        }
    }

    SignUpScreen(
        name = state.name,
        email = state.email,
        password = state.password,
        confirmPassword = state.confirmPassword,
        onEvent = viewModel::onEvent,
        emailError = state.emailError,
        passwordError = state.passwordError,
        confirmPasswordError = state.confirmPasswordError
    )
}

@Composable
fun SignUpScreen(
    name: String = "",
    email: String = "",
    password: String = "",
    confirmPassword: String = "",
    onEvent: (SignUpEvent) -> Unit = {},
    emailError: UiText.StringResource? = null,
    passwordError: UiText.StringResource? = null,
    confirmPasswordError: UiText.StringResource? = null,
    context: Context = LocalContext.current
) {
    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            color = MaterialTheme.colorScheme.secondaryContainer,
            fontSize = 64.sp,
            textAlign = TextAlign.Start
        )

        Text(
            text = stringResource(R.string.create_your_account),
            color = Black20,
            fontSize = 20.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(34.dp))

        TextField(
            value = name,
            onValueChange = {
                onEvent(SignUpEvent.OnNameChange(it))
            },
            modifier = textFieldShadow.focusRequester(focusRequester),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.enter_name)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.name),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                focusedTextColor = Black20,
                unfocusedTextColor = Black20,
                errorContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                errorTextColor = Black20
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        TextField(
            value = email,
            onValueChange = {
                onEvent(SignUpEvent.OnEmailChange(it))
            },
            modifier = textFieldShadow,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.enter_email)
                )
            },
            isError = emailError != null,
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
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                focusedTextColor = Black20,
                unfocusedTextColor = Black20,
                errorContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                errorTextColor = Black20
            )
        )

        if (emailError != null) {
            Text(
                text = emailError.asString(context = context),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        TextField(
            value = password,
            onValueChange = {
                onEvent(SignUpEvent.OnPasswordChange(it))
            },
            modifier = textFieldShadow,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(R.string.enter_password)
                )
            },
            isError = passwordError != null,
            label = {
                Text(
                    text = stringResource(R.string.password),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                focusedTextColor = Black20,
                unfocusedTextColor = Black20,
                errorContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                errorTextColor = Black20
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        if (passwordError != null) {
            Text(
                text = passwordError.asString(context = context),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        TextField(
            value = confirmPassword,
            onValueChange = {
                onEvent(SignUpEvent.OnConfirmPasswordChange(it))
            },
            modifier = textFieldShadow,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(R.string.confirm_your_password)
                )
            },
            isError = confirmPasswordError != null,
            label = {
                Text(
                    text = stringResource(R.string.confirm_password),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onEvent(SignUpEvent.OnSignUpClick)
                }
            ),
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                focusedTextColor = Black20,
                unfocusedTextColor = Black20,
                errorContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                errorTextColor = Black20
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        if (confirmPasswordError != null) {
            Text(
                text = confirmPasswordError.asString(context = context),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                onEvent(SignUpEvent.OnSignUpClick)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                color = Color.White
            )
        }

        Image(
            painter = painterResource(id = R.drawable.people_recycling_trash),
            contentDescription = stringResource(R.string.people_recycling_image),
            modifier = Modifier
                .fillMaxWidth()
                .size(600.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Preview
@Composable
private fun SignupScreenPreview() {
    SignUpScreen()
}