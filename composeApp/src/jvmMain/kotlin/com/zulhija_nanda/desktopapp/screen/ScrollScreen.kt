package com.zulhija_nanda.desktopapp.screen

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.zulhija_nanda.desktopapp.components.KeyEventContent
import com.zulhija_nanda.desktopapp.components.MouseHoverContent
import com.zulhija_nanda.desktopapp.components.Tooltips

class ScrollScreen: Screen {

    @Composable
    override fun Content() {
        MouseHoverContent()
//        KeyEventContent()
//        Tooltips()
//        Scrollablelist()
//        ScrollableLazylist()
    }
}

@Composable
fun Scrollablelist(){
    val verticaalScroll = rememberScrollState(0)
    val horizontalScroll = rememberScrollState(0)

    Box (
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 12.dp, bottom = 12.dp)
                .verticalScroll(verticaalScroll)
                .horizontalScroll(horizontalScroll)
        ) {
            for(item in 0 .. 30){
                Text(text = "Item nomor #$item")
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = verticaalScroll)
        )
        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(end = 12.dp),
            adapter = rememberScrollbarAdapter(horizontalScroll)
        )
    }

}

@Composable
fun ScrollableLazylist(){
    val verticaalScroll = rememberLazyListState(0)
    val horizontalScroll = rememberScrollState(0)

    Box (
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 12.dp, bottom = 12.dp)
                .horizontalScroll(horizontalScroll)
        ) {
            items(1000){ number ->
                Text(text = "Item nomor #$number")

            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = verticaalScroll)
        )
        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(end = 12.dp),
            adapter = rememberScrollbarAdapter(horizontalScroll)
        )
    }
}