package com.zulhija_nanda.desktopapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.zulhija_nanda.desktopapp.components.DeleteProductDialog
import com.zulhija_nanda.desktopapp.components.EmptyProductState
import com.zulhija_nanda.desktopapp.components.ProductCard
import com.zulhija_nanda.desktopapp.components.StatusBar
import com.zulhija_nanda.product.shared.di.SharedContainer
import com.zulhija_nanda.product.shared.model.Product
import com.zulhija_nanda.product.shared.util.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        val online by NetworkMonitor.isOnline.collectAsState()
        var searchQuery by remember { mutableStateOf("") }
        var showDeleteDialog by remember { mutableStateOf<Product?>(null) }
        var pendingCount by remember { mutableStateOf(0L) }

        // Update pending count
        LaunchedEffect(online) {
            withContext(Dispatchers.IO) {
                pendingCount = SharedContainer.syncManager.pendingCount()
            }
        }

        // Filter products based on search
        val filteredProducts = remember(products, searchQuery) {
            if (searchQuery.isEmpty()) products
            else products.filter {
                it.title?.contains(searchQuery, ignoreCase = true) == true ||
                        it.description?.contains(searchQuery, ignoreCase = true) == true
            }
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator?.push(FormScreen()) },
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Product")
                }
            }
        ) { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                // Enhanced Status Bar
                StatusBar(online, pendingCount)

                // Search and Actions Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 2.dp,
                    color = Color(0xFFF5F5F5)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Search Field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Search products...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, "Search")
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, "Clear")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
//                            colors = OutlinedTextFieldDefault.colors(
//                                focusedContainerColor = Color.White,
//                                unfocusedContainerColor = Color.White
//                            )
                        )

                        // Force Sync Button
                        IconButton(
                            onClick = {
                                scope.launch(Dispatchers.IO) {
                                    // SharedContainer.syncManager.syncIfOnline(online)
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color(0xFF1976D2),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                "Force Sync",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Product Count & Filter Info
                if (filteredProducts.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${filteredProducts.size} product${if (filteredProducts.size != 1) "s" else ""}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )

                        if (searchQuery.isNotEmpty()) {
                            Text(
                                text = "Filtered from ${products.size} total",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Product List or Empty State
                if (filteredProducts.isEmpty()) {
                    EmptyProductState(
                        searchQuery = searchQuery,
                        hasProducts = products.isNotEmpty(),
                        onAddClick = { navigator?.push(FormScreen()) }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = filteredProducts,
                            key = { it.localId ?: it.serverId ?: it.hashCode() }
                        ) { product ->
                            ProductCard(
                                product = product,
                                online = online,
                                onClick = { navigator?.push(FormScreen(product)) },
                                onDelete = { showDeleteDialog = product }
                            )
                        }
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        showDeleteDialog?.let { product ->
            DeleteProductDialog(
                product = product,
                onConfirm = {
                    scope.launch(Dispatchers.IO) {
                        SharedContainer.repository.delete(product)
                    }
                    showDeleteDialog = null
                },
                onDismiss = { showDeleteDialog = null }
            )
        }
    }
}
