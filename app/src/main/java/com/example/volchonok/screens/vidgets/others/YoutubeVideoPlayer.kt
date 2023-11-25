package com.example.volchonok.screens.vidgets.others

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

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

    AndroidView(factory = { webView }) { view ->
        view.loadDataWithBaseURL(
            "",
            htmlData,
            "text/html",
            "UTF-8",
            null
        )
    }
}

private fun getHTMLData(videoId: String): String {
    return """
        <html>
            <head>
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                    }
                    #player {
                        width: 100%;
                        max-width: 100%;
                        aspect-ratio: 16 / 9;
                        overflow: hidden;
                    }
                    iframe {
                        width: 100%;
                        height: 100%;
                    }
                </style>
            </head>
            <body>
                <div id="player">
                    <iframe
                        src='https://www.youtube.com/embed/$videoId?showinfo=0'
                        allowfullscreen  
                    />
                </div>
            </body>
        </html>
    """.trimIndent()
}
