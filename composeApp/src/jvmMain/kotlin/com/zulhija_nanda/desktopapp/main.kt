package com.zulhija_nanda.desktopapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Desktop Product App",
    ) {
        App()
    }
}