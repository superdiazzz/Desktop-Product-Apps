package com.zulhija_nanda.product.shared.network

import com.zulhija_nanda.product.shared.model.Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ProductApi(
    private val client: HttpClient,
    private val userId: String
) {
    private val base = "https://multitenant-apis-production.up.railway.app"

    suspend fun fetch(): List<Product> =
        client.get("$base/products/$userId").body()

    suspend fun create(p: Product) =
        client.post("$base/products/$userId") { setBody(p) }

    suspend fun delete(id: String) =
        client.delete("$base/products/$userId/$id")
}