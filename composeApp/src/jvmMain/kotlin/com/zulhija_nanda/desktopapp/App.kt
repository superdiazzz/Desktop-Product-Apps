package com.zulhija_nanda.desktopapp

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.zulhija_nanda.desktopapp.screen.HomeScreen
import com.zulhija_nanda.product.shared.di.SharedContainer
import com.zulhija_nanda.product.shared.util.NetworkMonitor

@Composable
@Preview
fun App() {

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        NetworkMonitor.start(scope)

        NetworkMonitor.isOnline.collect { online ->
            if(online){
                SharedContainer.repository.refreshFromRemote()
                SharedContainer.syncManager.syncIfOnline(true)
            }
        }
    }

    MaterialTheme {
        Navigator(HomeScreen()){ navigator ->
            SlideTransition(navigator)
        }
    }
}