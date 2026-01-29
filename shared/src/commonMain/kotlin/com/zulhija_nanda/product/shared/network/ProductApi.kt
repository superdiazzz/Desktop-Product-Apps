package com.zulhija_nanda.product.shared.network

import com.zulhija_nanda.product.shared.model.Product
import com.zulhija_nanda.product.shared.model.ProductListResponse
import com.zulhija_nanda.product.shared.model.ProductRequest
import com.zulhija_nanda.product.shared.model.ProductResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProductApi(
    private val client: HttpClient,
    private val userId: String
) {
    private val baseUrl = "https://multitenant-apis-production.up.railway.app"

    suspend fun fetchProducts(): List<Product> {
        val res: ProductListResponse =
            client.get("$baseUrl/products/$userId").body()

        return res.data.map {
            Product(
                serverId = it.id,
                title = it.title,
                description = it.description
            )
        }
    }

    suspend fun create(product: Product): ProductResponse {
        return client.post("$baseUrl/products/$userId") {
            contentType(ContentType.Application.Json)
            setBody(ProductRequest(product.title ?: "", product.description ?: ""))
        }.body() // Ambil body respon
    }
//    suspend fun create(product: Product) {
//        client.post("$baseUrl/products/$userId") {
//            contentType(ContentType.Application.Json)
//            setBody(
//                ProductRequest(
//                    title = product.title ?: "",
//                    description = product.description ?: ""
//                )
//            )
//        }
//    }

    suspend fun update(product: Product) {
        client.put("$baseUrl/products/$userId/${product.serverId}") {
            contentType(ContentType.Application.Json)
            setBody(
                ProductRequest(
                    title = product.title ?: "",
                    description = product.description ?: ""
                )
            )
        }
    }

    suspend fun delete(id: String) {
        client.delete("$baseUrl/products/$userId/$id")
    }
}