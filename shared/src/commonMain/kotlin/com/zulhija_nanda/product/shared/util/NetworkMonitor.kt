package com.zulhija_nanda.product.shared.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Socket

object NetworkMonitor {

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline

    private var job: Job? = null

    fun start(
        scope: CoroutineScope,
        intervalMs: Long = 3_000L
    ) {
        if (job != null) return

        job = scope.launch(Dispatchers.IO) {
            while (isActive) {
                _isOnline.value = checkInternet()
                delay(intervalMs)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    private fun checkInternet(): Boolean {
        return try {
            Socket().use {
                it.connect(
                    InetSocketAddress("8.8.8.8", 53),
                    1500
                )
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}