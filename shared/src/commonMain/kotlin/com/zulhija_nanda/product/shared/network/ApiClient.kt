package com.zulhija_nanda.product.shared.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

object ApiClient {
    fun create() = HttpClient {
        install(ContentNegotiation) { json() }
    }
}