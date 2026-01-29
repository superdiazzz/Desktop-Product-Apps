package com.zulhija_nanda.product.shared.repository

import com.zulhija_nanda.product.shared.model.Product
import com.zulhija_nanda.product.shared.network.ProductApi
import com.zulhija_nanda.product.shared.sync.SyncManager
import db.AppDatabase

class ProductRepository(
    private val db: AppDatabase,
    private val api: ProductApi,
    private val sync: SyncManager
) {
    fun localProducts() = db.productQueries.selectAll()

    suspend fun create(product: Product) {
        db.productQueries.insert(product.id, product.name, product.price)
        sync.enqueue("CREATE", product)
    }

    suspend fun syncRemote() {
        val remote = api.fetch()
        remote.forEach {
            db.productQueries.insert(it.id, it.name, it.price)
        }
    }
}
