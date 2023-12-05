package com.example.volchonok.screens.vidgets.others

import android.annotation.SuppressLint
import android.provider.MediaStore.Video
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.volchonok.R

private var showAvatarDialog = mutableStateOf(false)

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YoutubeVideoPlayer(videoId: String, showDialog: Boolean = true) {
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
    webView.loadUrl("javascript:playVideo();")

    if (showAvatarDialog.value && showDialog) VideoDialog(videoId)
}

@Composable
private fun VideoDialog(videoId: String) {
    AlertDialog(text = {
        YoutubeVideoPlayer(videoId, false)
    }, containerColor = MaterialTheme.colorScheme.background, onDismissRequest = {
        showAvatarDialog.value = false
    }, confirmButton = { })
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
