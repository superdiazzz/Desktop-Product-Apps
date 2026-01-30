package com.zulhija_nanda.desktopapp.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zulhija_nanda.product.shared.di.SharedContainer
import com.zulhija_nanda.product.shared.util.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class StatusBarConfig(
    val backgroundColor: Color,
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val showPulse: Boolean
)

@Composable
fun StatusBar(online: Boolean, pendingCount: Long) {
    val statusConfig = when {
        !online -> StatusBarConfig(
            backgroundColor = Color(0xFFD32F2F),
            icon = Icons.Default.CloudOff,
            title = "Offline Mode",
            subtitle = if (pendingCount > 0) "$pendingCount operation${if (pendingCount > 1L) "s" else ""} queued"
            else "Working offline",
            showPulse = false
        )
        pendingCount > 0 -> StatusBarConfig(
            backgroundColor = Color(0xFF1976D2),
            icon = Icons.Default.CloudSync,
            title = "Synchronizing",
            subtitle = "$pendingCount pending operation${if (pendingCount > 1L) "s" else ""}",
            showPulse = true
        )
        else -> StatusBarConfig(
            backgroundColor = Color(0xFF388E3C),
            icon = Icons.Default.CloudDone,
            title = "All Synced",
            subtitle = "Connected and up to date",
            showPulse = false
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = statusConfig.backgroundColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icon with pulse animation
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (statusConfig.showPulse) {
                        PulsingIcon(statusConfig.icon)
                    } else {
                        Icon(
                            imageVector = statusConfig.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = statusConfig.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = statusConfig.subtitle,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
            }

            // Status indicator dot
            StatusIndicatorDot(statusConfig.showPulse)
        }
    }
}
//@Composable
//fun StatusBar() {
//    val online by NetworkMonitor.isOnline.collectAsState()
//    var pending by remember { mutableStateOf(0L) }
//
//    LaunchedEffect(online) {
//        withContext(Dispatchers.IO) {
//            pending = SharedContainer.syncManager.pendingCount()
//        }
//    }
//
//    val color = when {
//        !online -> Color.Red
//        pending > 0 -> Color(0xFF2196F3)
//        else -> Color(0xFF2E7D32)
//    }
//
//    val text = when {
//        !online -> "Offline • Queue $pending"
//        pending > 0 -> "Syncing • Queue $pending"
//        else -> "Online • Synced"
//    }
//
//    Row(
//        Modifier
//            .fillMaxWidth()
//            .background(color)
//            .padding(8.dp)
//    ) {
//        Text(text, color = Color.White)
//    }
//}

@Composable
fun PulsingIcon(icon: ImageVector) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = Color.White.copy(alpha = alpha),
        modifier = Modifier.size(24.dp)
    )
}