package com.example.wastemanagementapp.core.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackBarEvent(
    val message: UiText.StringResource,
    val action: SnackBarAction? = null
)

data class SnackBarAction(
    val name: String,
    val action: suspend () -> Unit,
    val maxRetries: Int = 3
)

object SnackBarController {

    private val _events = Channel<SnackBarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackBarEvent) {
        _events.send(event)
    }
}

//suspend fun retryAction(action: SnackBarAction) {
//    var retryCount = 0
//    while (retryCount < action.maxRetries) {
//        Log.i("updateUserPoint", "retryAction: $retryCount")
//        try {
//            action.action()
//            return
//        } catch (e: Exception) {
//            retryCount++
//            if (retryCount < action.maxRetries) {
//                delay(1000L * retryCount) // Exponential backoff
//            }
//        }
//    }
//
//    SnackBarController.sendEvent(
//        SnackBarEvent(UiText.StringResource(R.string.retry_failed))
//    )
//}

