package com.zulhija_nanda.desktopapp.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MouseHoverContent(){
    Column(
        Modifier.background(Color.White),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(10){ index ->
            var hovered by remember { mutableStateOf(false) }
            val animatedColor by animateColorAsState(
                targetValue = if(hovered) Color.Cyan else Color.White,
                animationSpec = tween(200)
            )
            Text(
                modifier = Modifier.fillMaxWidth()
                    .background(animatedColor)
                    .padding(all = 8.dp)
                    .onPointerEvent(PointerEventType.Enter) { hovered = true }
                    .onPointerEvent(PointerEventType.Exit) { hovered = false },
                fontSize = 30.sp,
                text = "Item number $index"
            )
        }
    }
}