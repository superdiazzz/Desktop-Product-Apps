package com.zulhija_nanda.desktopapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.isTraySupported
import androidx.compose.ui.window.rememberTrayState
import desktop_product_app.composeapp.generated.resources.Res
import desktop_product_app.composeapp.generated.resources.icon_product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.compose.resources.painterResource

fun main() = application {

    // Coroutine scope global untuk app lifecycle
    val appScope = remember {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
    val icon = painterResource(Res.drawable.icon_product)
    val trayState = rememberTrayState()

    if(isTraySupported){
        Tray(
            icon = icon,
            menu = {
                Item(
                    text = "Send Notification",
                    onClick = {
                        trayState.sendNotification(
                            Notification(
                                title = "New Notification!",
                                message = "You got new message!"
                            )
                        )
                    }
                )
                Item(
                    text = "Exit",
                    onClick = ::exitApplication
                )
            }
        )
    }


    Window(
        onCloseRequest = ::exitApplication,
        title = "Desktop Product App",
    ) {
        App()
    }
}