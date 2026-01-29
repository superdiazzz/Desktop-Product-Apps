package com.zulhija_nanda.desktopapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.zulhija_nanda.desktopapp.components.StatusBar
import com.zulhija_nanda.product.shared.di.SharedContainer
import com.zulhija_nanda.product.shared.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val scope = rememberCoroutineScope()
        val products by SharedContainer
            .repository
            .observeProducts()
            .collectAsState(initial = emptyList())

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator?.push(FormScreen()) }
                ) {
                    Icon(Icons.Default.Add, null)
                }
            }
        ) { paddingValues ->
            Column (Modifier.padding(paddingValues)){
                StatusBar()

                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
//                        SharedContainer.syncManager.syncIfOnline(online.value)
                    }
                }) {
                    Text("Force Sync")
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navigator?.push(
                                        FormScreen(product)
                                    )
                                }
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(product.title ?: "", style = MaterialTheme.typography.titleMedium)
                                Text("Description: ${product.description}")
                            }
                        }
                    }
                }

            }
        }


    }
}
