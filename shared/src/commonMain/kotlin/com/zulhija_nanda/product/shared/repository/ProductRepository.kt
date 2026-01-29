package com.zulhija_nanda.product.shared.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.zulhija_nanda.product.shared.di.SharedContainer.api
import com.zulhija_nanda.product.shared.model.Product
import com.zulhija_nanda.product.shared.network.ProductApi
import com.zulhija_nanda.product.shared.sync.SyncManager
import db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepository(
    private val db: AppDatabase,
    private val api: ProductApi,
    private val sync: SyncManager
) {

    fun observeProducts(): Flow<List<Product>> =
        db.productQueries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows ->
                rows.map {
                    Product(
                        localId = it.localId,
                        serverId = it.serverId?.toInt(),
                        title = it.title,
                        description = it.description
                    )
                }
            }

    /**
     * Pull latest data from API and reconcile local DB
     * (dipanggil saat app start / online)
     */
    suspend fun refreshFromRemote() {
        val remote = api.fetchProducts()

        db.transaction {
            db.productQueries.deleteAll()

            remote.forEach {
                db.productQueries.insertLocal(
                    it.serverId?.toLong(),
                    it.title ?: "",
                    it.description
                )
            }
        }
    }


    /**
     * OFFLINE-FIRST create
     */
    suspend fun create(product: Product) {
        val localId = db.transactionWithResult {
            db.productQueries.insertLocal(
                null,
                product.title ?: "",
                product.description
            )
            // Panggil fungsi lastInsertId yang baru kita buat
            db.productQueries.lastInsertId().executeAsOne()
        }

        sync.enqueue(
            "CREATE",
            product.copy(localId = localId)
        )
    }

    /**
     * Update local immediately, queue sync
     */
    suspend fun update(product: Product) {
        requireNotNull(product.localId)

        db.productQueries.updateByLocalId(
            product.title ?: "",
            product.description,
            product.localId
        )

        sync.enqueue("UPDATE", product)
    }

    /**
     * Delete local immediately, queue sync
     */
    suspend fun delete(product: Product) {
        requireNotNull(product.localId)

        db.productQueries.deleteByLocalId(product.localId)

        sync.enqueue("DELETE", product)
    }
}
