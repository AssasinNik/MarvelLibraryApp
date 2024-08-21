package com.example.marvel_app.presentation.reusable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.kevinnzou.web.AccompanistWebViewClient
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import timber.log.Timber


@Composable
fun YoutubeVideoPlayer(videoId: String, context: Context) {
    var isLoading by remember { mutableStateOf(true) }

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    isLoading = true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    isLoading = false
                }
            }
        }
    }

    val htmlData = getHTMLData(videoId)
    if (isLoading) {
        CircularProgressIndicator(
            color = SearchBorderColor,
            modifier = Modifier
                .padding(top = 10.dp)
        )
    }
    AndroidView(
        factory = { webView },
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .alpha(if (isLoading) 0f else 1f)
    ) { view ->
        view.loadDataWithBaseURL(
            "https://www.youtube.com",
            htmlData,
            "text/html",
            "UTF-8",
            null
        )
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
                            height: '675',
                            width: '1000',
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

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrightcovePlayer(
    initialUrl: String,
    context: Context
) {
    val state = rememberWebViewState(url = initialUrl)
    val navigator = rememberWebViewNavigator()
    var isLoading by remember { mutableStateOf(true) }

    Column {
        // Placeholder or loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = SearchBorderColor,
                    modifier = Modifier
                        .padding(top = 10.dp)
                ) // Индикатор загрузки
            }
        }

        // WebView отображается только когда страница загружена
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(240.dp)
                .clip(RoundedCornerShape(10.dp))
                .alpha(if (isLoading) 0f else 1f), // Прозрачность для плавного перехода
            navigator = navigator,
            onCreated = { webView ->
                webView.settings.javaScriptEnabled = true
            },
            client = remember {
                object : AccompanistWebViewClient() {
                    override fun onPageStarted(
                        view: WebView,
                        url: String?,
                        favicon: Bitmap?
                    ) {
                        super.onPageStarted(view, url, favicon)
                        Timber.tag("Accompanist WebView").d("Page started loading for $url")
                        isLoading = true
                    }
                    override fun onPageFinished(view: WebView, url: String?) {
                        if (view != null) {
                            super.onPageFinished(view, url)
                        }
                        Timber.tag("Accompanist WebView").d("Page finished loading for $url")
                        isLoading = false
                    }
                }
            }
        )
    }
}
