package com.example.wastemanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wastemanagementapp.auth.presentation.LoginScreenContainer
import com.example.wastemanagementapp.auth.presentation.SignUpScreenContainer
import com.example.wastemanagementapp.core.util.ObserveAsEvents
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.core.util.SnackBarController
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

                val navController = rememberNavController()

                val scope = rememberCoroutineScope()
                ObserveAsEvents(
                    flow = SnackBarController.events,
                    snackBarHostState
                ) { event ->
                    scope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()

                        val result = snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action?.name,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
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
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.LoginScreen,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable<Screen.LoginScreen> {
                                LoginScreenContainer(
                                    context = LocalContext.current,
                                    scope = CoroutineScope(Dispatchers.Main),
                                    onNavigate = {
                                        navController.navigate(it.screen)
                                    }
                                )
                            }

                            composable<Screen.SignUpScreen> {
                                SignUpScreenContainer()
                            }
                        }
                    }
                }
            }
        }
    }
}
