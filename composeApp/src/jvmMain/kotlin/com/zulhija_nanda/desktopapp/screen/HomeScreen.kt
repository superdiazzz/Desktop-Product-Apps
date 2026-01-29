package com.zulhija_nanda.desktopapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import com.zulhija_nanda.desktopapp.components.StatusBar
import com.zulhija_nanda.product.shared.di.SharedContainer
import com.zulhija_nanda.product.shared.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val online = remember { mutableStateOf(true) }
        val scope = rememberCoroutineScope()


        Column {
            StatusBar(online.value)
            Button(onClick = {
                scope.launch {
                    SharedContainer.repository.create(
                        Product(UUID.randomUUID().toString(), "Offline Item", 10.0)
                    )
                }
            }) {
                Text("Add Product (Offline)")
            }

            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    SharedContainer.syncManager.syncIfOnline(online.value)
                }
            }) {
                Text("Force Sync")
            }

        }
    }
}
