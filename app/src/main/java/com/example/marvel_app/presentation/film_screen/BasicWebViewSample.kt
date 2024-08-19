package com.example.marvel_app.presentation.film_screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kevinnzou.web.AccompanistWebViewClient
import com.kevinnzou.web.LoadingState
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState

class BasicWebViewSample : ComponentActivity() {
    val initialUrl = "https://players.brightcove.net/5359769168001/rkg9u15t7b_default/index.html?videoId=6027157990001"

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = rememberWebViewState(url = initialUrl)
            val navigator = rememberWebViewNavigator()
            var textFieldValue by remember(state.lastLoadedUrl) {
                mutableStateOf(state.lastLoadedUrl)
            }

            Column {
                TopAppBar(
                    title = { Text(text = "WebView Sample") },
                    navigationIcon = {
                        if (navigator.canGoBack) {
                            IconButton(onClick = { navigator.navigateBack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    }
                )

                Row {
                    Box(modifier = Modifier.weight(1f)) {
                        if (state.errorsForCurrentRequest.isNotEmpty()) {
                            Image(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                colorFilter = ColorFilter.tint(Color.Red),
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(8.dp)
                            )
                        }

                        OutlinedTextField(
                            value = textFieldValue ?: "",
                            onValueChange = { textFieldValue = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(
                        onClick = {
                            textFieldValue?.let {
                                navigator.loadUrl(it)
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text("Go")
                    }
                }

                val loadingState = state.loadingState
                if (loadingState is LoadingState.Loading) {
                    LinearProgressIndicator(
                        progress = loadingState.progress,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // A custom WebViewClient and WebChromeClient can be provided via subclassing
                val webClient = remember {
                    object : AccompanistWebViewClient() {
                        override fun onPageStarted(
                            view: WebView,
                            url: String?,
                            favicon: Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)
                            Log.d("Accompanist WebView", "Page started loading for $url")
                        }
                    }
                }

                WebView(
                    state = state,
                    modifier = Modifier
                        .weight(1f),
                    navigator = navigator,
                    onCreated = { webView ->
                        webView.settings.javaScriptEnabled = true
                    },
                    client = webClient
                )
            }
        }
    }
}
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YoutubeVideoPlayer(videoId: String) {
    val webView = WebView(LocalContext.current).apply {
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        webViewClient = WebViewClient()
    }

    val htmlData = getHTMLData(videoId)

    Column(Modifier.fillMaxSize()) {

        AndroidView(factory = { webView }) { view ->
            view.loadDataWithBaseURL(
                "https://www.youtube.com",
                htmlData,
                "text/html",
                "UTF-8",
                null
            )
        }
        Button(onClick = {
            webView.loadUrl("javascript:playVideo();")
        }) {
            Text(text = "Play Video")
        }
        Button(onClick = {
            webView.loadUrl("javascript:pauseVideo();")
        }) {
            Text(text = "Pause Video")
        }
        Button(onClick = {
            webView.loadUrl("javascript:seekTo(60);")
        }) {
            Text(text = "Seek Video")
        }
    }
}

fun getHTMLData(videoId: String): String {
    return """
        <html>
        
            <body style="margin:0px;padding:0px;">
               <div id="player"></div>
                <script>
                    var player;
                    function onYouTubeIframeAPIReady() {
                        player = new YT.Player('player', {
                            height: '450',
                            width: '650',
                            videoId: '$videoId',
                            playerVars: {
                                'playsinline': 1
                            },
                            events: {
                                'onReady': onPlayerReady
                            }
                        });
                    }
                    function onPlayerReady(event) {
                     player.playVideo();
                        // Player is ready
                    }
                    function seekTo(time) {
                        player.seekTo(time, true);
                    }
                      function playVideo() {
                        player.playVideo();
                    }
                    function pauseVideo() {
                        player.pauseVideo();
                    }
                </script>
                <script src="https://www.youtube.com/iframe_api"></script>
            </body>
        </html>
    """.trimIndent()
}