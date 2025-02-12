package com.example.wastemanagementapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wastemanagementapp.auth.presentation.screens.LoginScreenContainer
import com.example.wastemanagementapp.auth.presentation.screens.SignUpScreenContainer
import com.example.wastemanagementapp.auth.presentation.viewmodel.LoginViewModel
import com.example.wastemanagementapp.complaint.presentation.screens.ComplaintScreenContainer
import com.example.wastemanagementapp.core.util.ObserveAsEvents
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.core.util.SnackBarController
import com.example.wastemanagementapp.faq.presentation.FAQContainer
import com.example.wastemanagementapp.feedback.presentation.FeedbackContainer
import com.example.wastemanagementapp.home.presentation.HomeScreenContainer
import com.example.wastemanagementapp.support.presentation.SupportContainer
import com.example.wastemanagementapp.ui.theme.WasteManagementAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WasteManagementAppTheme {
                val snackBarHostState = remember {
                    SnackbarHostState()
                }

                var isBottomBarActive by remember {
                    mutableStateOf(false)
                }

                Log.i("ui", "onCreate: $isBottomBarActive")

                val loginViewModel: LoginViewModel = hiltViewModel()
                val authState by loginViewModel.authState.collectAsStateWithLifecycle()

                val navController = rememberNavController()

                val scope = rememberCoroutineScope()
                ObserveAsEvents(
                    flow = SnackBarController.events,
                    snackBarHostState
                ) { event ->
                    scope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()

                        val result = snackBarHostState.showSnackbar(
                            message = event.message.asString(this@MainActivity),
                            actionLabel = event.action?.name,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }

                LaunchedEffect(authState) {
                    if (authState != null && authState!!.isEmailVerified) {
                        navController.navigate(Screen.HomeScreen) {
                            popUpTo(Screen.LoginScreen) { inclusive = true }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(hostState = snackBarHostState)
                        },
                        bottomBar = {
                            if (isBottomBarActive) {
                                AppBottomBar(
                                    onClick = { navEvent ->
                                        navController.navigate(navEvent.screen)
                                    }
                                )
                            }
                        },
                        floatingActionButton = {
                            if (isBottomBarActive) {
                                FloatingActionButton(
                                    onClick = { navController.navigate(Screen.HomeScreen) },
                                    containerColor = MaterialTheme.colorScheme.inversePrimary,
                                    contentColor = Color.White,
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .offset(y = 40.dp)
                                        .size(52.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.home),
                                        contentDescription = stringResource(R.string.home_icon)
                                    )
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.Center
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = if (authState != null && authState!!.isEmailVerified) {
                                Screen.HomeScreen
                            } else {
                                Screen.LoginScreen
                            },
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable<Screen.LoginScreen> {
                                isBottomBarActive = false
                                LoginScreenContainer(
                                    context = LocalContext.current,
                                    scope = CoroutineScope(Dispatchers.Main),
                                    onNavigate = {
                                        navController.navigate(it.screen)
                                    }
                                )
                            }

                            composable<Screen.SignUpScreen> {
                                isBottomBarActive = false
                                SignUpScreenContainer(
                                    onNavigate = {
                                        navController.navigate(it.screen)
                                    }
                                )
                            }

                            composable<Screen.HomeScreen> {
                                isBottomBarActive = true
                                HomeScreenContainer(
                                    onNavigate = { event ->
                                        navController.navigate(event.screen)
                                    }
                                )
                            }

                            composable<Screen.TrackScreen> {
                                isBottomBarActive = true
                                Text("Hello")
                            }

                            composable<Screen.ScheduleScreen> {
                                isBottomBarActive = true
                                Text("Jello")
                            }

                            composable<Screen.ComplaintScreen> {
                                isBottomBarActive = false
                                ComplaintScreenContainer()
                            }

                            composable<Screen.FeedbackScreen> {
                                isBottomBarActive = false
                                FeedbackContainer()
                            }

                            composable<Screen.FaqScreen> {
                                isBottomBarActive = true
                                FAQContainer()
                            }

                            composable<Screen.SupportScreen> {
                                isBottomBarActive = true
                                SupportContainer(
                                    onNavigate = { event ->
                                        navController.navigate(event.screen)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
