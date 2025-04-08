package com.example.wastemanagementapp.track.presentation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun TrackScreenContainer() {
    TrackScreen()
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TrackScreen() {
    val url = "http://13.232.13.233/jsp/VehicleLiveTracking.jsp?username=MCC&password=Mcc@123&vehicle_no=KA19AE2082"
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    val myWebView = remember { WebView(context).apply {
        // Initial configuration moved here for better reliability
        with(settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            //databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            allowContentAccess = true
            allowFileAccess = true
            //allowFileAccessFromFileURLs = true
            //allowUniversalAccessFromFileURLs = true
            mediaPlaybackRequiresUserGesture = false
            cacheMode = WebSettings.LOAD_DEFAULT // Changed back to DEFAULT for map tiles
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Mobile Safari/537.36"

            // Important for map rendering
            setGeolocationEnabled(true)
            //setRenderPriority(WebSettings.RenderPriority.HIGH)
        }

        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                isLoading = true
                hasError = false
            }

            override fun onPageFinished(view: WebView, url: String?) {
                isLoading = false
                // Additional check for map rendering
                postDelayed({
                    evaluateJavascript("""
                        if (typeof window.mapInitComplete === 'undefined') {
                            console.warn('Map initialization not complete');
                            // Try forcing map redraw
                            if (typeof map !== 'undefined') {
                                map.invalidateSize();
                                console.log('Triggered map redraw');
                            }
                        }
                    """.trimIndent(), null)
                }, 2000)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                Log.e("WebView", "Error loading ${request.url}: ${error.description}")
                if (request.isForMainFrame) {
                    hasError = true
                }
            }

            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                Log.d("WebView", "Loading resource: ${request.url}")
                return super.shouldInterceptRequest(view, request)
            }
        }

        webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("WebViewConsole", consoleMessage.message())
                return true
            }

            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                callback.invoke(origin, true, false) // Always allow geolocation
            }
        }

        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }}

    DisposableEffect(Unit) {
        WebView.setWebContentsDebuggingEnabled(true)

        onDispose {
            myWebView.stopLoading()
            myWebView.webViewClient = WebViewClient()
            myWebView.webChromeClient = WebChromeClient()
        }
    }

    Column(Modifier.fillMaxSize()) {
        if (isLoading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }

        if (hasError) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Failed to load tracking data", color = MaterialTheme.colorScheme.error)
                Button(
                    onClick = {
                        hasError = false
                        myWebView.reload()
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Retry")
                }
            }
        }

        AndroidView(
            factory = { context ->
                FrameLayout(context).apply {
                    if (myWebView.parent != null) {
                        (myWebView.parent as ViewGroup).removeView(myWebView)
                    }
                    addView(myWebView, FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    ))

                    // Load with cache-busting and additional headers
                    myWebView.loadUrl(url, mapOf(
                        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                        "Accept-Language" to "en-US,en;q=0.5",
                        "Referer" to url // Important for some map services
                    ))
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
