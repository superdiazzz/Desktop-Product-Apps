package com.zulhija_nanda.desktopapp

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.zulhija_nanda.desktopapp.screen.ScrollScreen

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