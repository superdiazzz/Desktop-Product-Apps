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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
@Preview
fun App() {

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        NetworkMonitor.start(scope)

        NetworkMonitor.isOnline
            .filter { it }
            .collect {
                // Sync dulu (dorong offline changes)
                SharedContainer.syncManager.syncIfOnline(true)

                //  Baru pull data dari server
                SharedContainer.repository.refreshFromRemote()
            }
    }

    MaterialTheme {
        Navigator(HomeScreen()){ navigator ->
            SlideTransition(navigator)
        }
    }
}