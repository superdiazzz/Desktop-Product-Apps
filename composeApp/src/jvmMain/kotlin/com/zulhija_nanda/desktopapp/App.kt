package com.zulhija_nanda.desktopapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.zulhija_nanda.desktopapp.api.ApiService
import com.zulhija_nanda.desktopapp.screen.HomeScreen
import com.zulhija_nanda.desktopapp.screen.ScrollScreen
import org.jetbrains.compose.resources.painterResource
import desktop_product_app.composeapp.generated.resources.Res
import desktop_product_app.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(ScrollScreen()){ navigator ->
            SlideTransition(navigator)
        }
//        var showContent by remember { mutableStateOf(false) }
//        var apiResponse by remember { mutableStateOf("Waiting...") }
//        val scope = rememberCoroutineScope()
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = {
//                //showContent = !showContent
//                scope.launch {
//                    apiResponse = ApiService().fetchData()
//                }
//            }) {
//                Text("Click me!")
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//            Text("${apiResponse}")
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $apiResponse")
//                }
//            }
//        }
    }
}