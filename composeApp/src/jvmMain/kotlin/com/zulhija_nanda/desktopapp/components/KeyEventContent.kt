package com.zulhija_nanda.desktopapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp

@Composable
fun KeyEventContent(){
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text1,
            onValueChange = { text1 = it},
            modifier = Modifier.onPreviewKeyEvent {
                if(it.key == Key.Delete && it.type == KeyEventType.KeyDown){
                    text1 = ""
                    true
                }else false
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = text2,
            onValueChange = { text2 = it},

        )
    }
}