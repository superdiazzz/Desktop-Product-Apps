package com.zulhija_nanda.desktopapp.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.zulhija_nanda.desktopapp.components.CancelConfirmationDialog
import com.zulhija_nanda.desktopapp.components.ProductPreviewCard
import com.zulhija_nanda.product.shared.di.SharedContainer
import com.zulhija_nanda.product.shared.model.Product
import com.zulhija_nanda.product.shared.util.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class FormScreen(
    private val existing: Product? = null
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val scope = rememberCoroutineScope()
        val online by NetworkMonitor.isOnline.collectAsState()

        var title by remember { mutableStateOf(existing?.title ?: "") }
        var desc by remember { mutableStateOf(existing?.description ?: "") }
        var titleError by remember { mutableStateOf(false) }
        var showCancelDialog by remember { mutableStateOf(false) }

        val isEditMode = existing != null
        val hasChanges = title != (existing?.title ?: "") || desc != (existing?.description ?: "")

        Scaffold(
            topBar = {
                // Enhanced Top Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 2.dp,
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (hasChanges) {
                                        showCancelDialog = true
                                    } else {
                                        navigator?.pop()
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ArrowBack, "Back")
                            }
                            Column {
                                Text(
                                    text = if (isEditMode) "Edit Product" else "New Product",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (online) Icons.Default.CloudDone else Icons.Default.CloudOff,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = if (online) Color(0xFF388E3C) else Color(0xFFF57C00)
                                    )
                                    Text(
                                        text = if (online) "Changes will sync immediately"
                                        else "Changes will sync when online",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (online) Color(0xFF388E3C) else Color(0xFFF57C00)
                                    )
                                }
                            }
                        }

                        // Save Button
                        Button(
                            onClick = {
                                if (title.isBlank()) {
                                    titleError = true
                                } else {
                                    val product = if (isEditMode) {
                                        existing?.copy(
                                            title = title,
                                            description = desc
                                        )
                                    } else {
                                        Product(
                                            title = title,
                                            description = desc
                                        )
                                    }

                                    scope.launch(Dispatchers.IO) {
                                        product?.let {
                                            if (isEditMode) {
                                                SharedContainer.repository.update(product, online)
                                            } else {
                                                SharedContainer.repository.create(product, online)
                                            }
                                        }
                                    }
                                    navigator?.pop()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            enabled = title.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2)
                            )
                        ) {
                            Icon(
                                Icons.Default.Save,
                                "Save",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Save")
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Information Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(24.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Product Information",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2),
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (online) {
                                    "Fill in the details below. Your product will be saved and synced to the server."
                                } else {
                                    "Fill in the details below. Your product will be saved locally and synced when you're back online."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF1565C0)
                            )
                        }
                    }
                }

                // Title Field
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Product Title",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "* Required",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFD32F2F)
                        )
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            if (it.length <= 100) {
                                title = it
                                titleError = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter product name...") },
                        isError = titleError,
                        supportingText = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (titleError) {
                                    Text(
                                        text = "Product title is required",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    Text(
                                        text = "A clear, descriptive name for your product",
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    text = "${title.length}/100",
                                    color = if (title.length > 90) Color(0xFFF57C00) else Color.Gray
                                )
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.AutoMirrored.Filled.Label, "Title")
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = if (titleError) MaterialTheme.colorScheme.error
                            else Color(0xFF1976D2),
                            unfocusedBorderColor = if (titleError) MaterialTheme.colorScheme.error
                            else Color.LightGray
                        )
                    )
                }

                // Description Field
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Optional",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    OutlinedTextField(
                        value = desc,
                        onValueChange = {
                            if (it.length <= 500) {
                                desc = it
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        placeholder = { Text("Enter product description...") },
                        supportingText = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Provide detailed information about your product",
                                    color = Color.Gray
                                )
                                Text(
                                    text = "${desc.length}/500",
                                    color = if (desc.length > 450) Color(0xFFF57C00) else Color.Gray
                                )
                            }
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Description,
                                "Description",
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF1976D2),
                            unfocusedBorderColor = Color.LightGray
                        ),
                        maxLines = 8
                    )
                }

                // Preview Card (only show if title is not empty)
                if (title.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Preview",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        ProductPreviewCard(
                            title = title,
                            description = desc,
                            isPendingSync = !online
                        )
                    }
                }

                // Offline Warning
                if (!online) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF3E0)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = Color(0xFFF57C00),
                                modifier = Modifier.size(24.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Working Offline",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF57C00),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Your product will be saved locally and automatically synced to the server when you reconnect to the internet.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFE65100)
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            if (hasChanges) {
                                showCancelDialog = true
                            } else {
                                navigator?.pop()
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                titleError = true
                            } else {
                                val product = if (isEditMode) {
                                    existing?.copy(
                                        title = title,
                                        description = desc
                                    )
                                } else {
                                    Product(
                                        title = title,
                                        description = desc
                                    )
                                }

                                scope.launch(Dispatchers.IO) {
                                    if (isEditMode) {
                                        product?.let {
                                            SharedContainer.repository.update(product, online)
                                        }
                                    } else {
                                        product?.let {
                                            SharedContainer.repository.create(product, online)
                                        }
                                    }
                                }
                                navigator?.pop()
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = title.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2),
                            disabledContainerColor = Color.LightGray
                        )
                    ) {
                        Icon(
                            Icons.Default.Save,
                            "Save",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(if (isEditMode) "Update Product" else "Create Product")
                    }
                }
            }
        }

        // Cancel Confirmation Dialog
        if (showCancelDialog) {
            CancelConfirmationDialog(
                onConfirm = { navigator?.pop() },
                onDismiss = { showCancelDialog = false }
            )
        }
    }
}