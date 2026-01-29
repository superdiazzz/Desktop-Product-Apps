package com.zulhija_nanda.product.shared.sync

import com.zulhija_nanda.product.shared.model.Product
import com.zulhija_nanda.product.shared.model.ProductResponse
import com.zulhija_nanda.product.shared.network.ProductApi
import db.AppDatabase
import kotlinx.serialization.json.Json

class SyncManager(
    private val db: AppDatabase,
    private val api: ProductApi
) {
    fun enqueue(type: String, product: Product) {
        db.queueQueries.insert(
            type,
            Json.encodeToString(Product.serializer(), product)
        )
    }

    suspend fun syncIfOnline(online: Boolean) {
        if (!online) return

        db.queueQueries.selectAll().executeAsList().forEach {
            val product = Json.decodeFromString(Product.serializer(), it.payload)
            when (it.type) {
                "CREATE" -> {
                    val response: ProductResponse = api.create(product)
                    db.productQueries.updateServerId(
                        response.id.toLong(),
                        product.localId!!
                    )                }
                "UPDATE" -> api.update(product)
                "DELETE" -> api.delete(product.serverId.toString())
            }
            db.queueQueries.deleteById(it.id)
        }
    }

    fun pendingCount(): Long =
        db.queueQueries.selectAll().executeAsList().size.toLong()
}
