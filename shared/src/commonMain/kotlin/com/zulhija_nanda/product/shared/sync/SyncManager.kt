package com.zulhija_nanda.product.shared.sync

import com.zulhija_nanda.product.shared.model.Product
import com.zulhija_nanda.product.shared.network.ProductApi
import db.AppDatabase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SyncManager(
    private val db: AppDatabase,
    private val api: ProductApi
) {
    fun enqueue(type: String, product: Product) {
        db.queueQueries.insert(
            type,
            Json.encodeToString(product)
        )
    }

    suspend fun syncIfOnline(online: Boolean) {
        if (!online) return

        db.queueQueries.selectAll().executeAsList().forEach {
            val product = Json.decodeFromString<Product>(it.payload)
            when (it.type) {
                "CREATE" -> api.create(product)
                "DELETE" -> api.delete(product.id)
            }
            db.queueQueries.deleteById(it.id)
        }
    }

    fun pendingCount(): Long =
        db.queueQueries.selectAll().executeAsList().size.toLong()
}
