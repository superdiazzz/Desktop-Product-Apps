package com.zulhija_nanda.desktopapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zulhija_nanda.product.shared.di.SharedContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun StatusBar(online: Boolean) {
    var pending by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            pending = SharedContainer.syncManager.pendingCount()
        }
    }

    val color = when {
        !online -> Color.Red
        pending > 0 -> Color.Blue
        else -> Color.Green
    }

    val text = when {
        !online -> "Offline • Queue $pending"
        pending > 0 -> "Syncing • Queue $pending"
        else -> "Online • Synced"
    }

    Row(
        Modifier
            .fillMaxWidth()
            .background(color)
            .padding(8.dp)
    ) {
        Text(text, color = Color.White)
    }
}