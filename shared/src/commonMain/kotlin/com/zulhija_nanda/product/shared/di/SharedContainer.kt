package com.zulhija_nanda.product.shared.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.zulhija_nanda.product.shared.network.ApiClient
import com.zulhija_nanda.product.shared.network.ProductApi
import com.zulhija_nanda.product.shared.repository.ProductRepository
import com.zulhija_nanda.product.shared.sync.SyncManager
import db.AppDatabase

object SharedContainer {
    private const val USER_ID = "cmkw67fpc000vapatwmql30ft"

    private val driver: JdbcSqliteDriver by lazy {
        val driver = JdbcSqliteDriver("jdbc:sqlite:app.db")

        try {
            driver.execute(
                null,
                "SELECT 1 FROM product LIMIT 1",
                0
            )
        } catch (e: Exception) {
            // DB BARU → CREATE SCHEMA
            AppDatabase.Schema.create(driver)
        }

        driver
    }

    val db: AppDatabase by lazy {
        AppDatabase(driver)
    }

    val api: ProductApi by lazy {
        ProductApi(ApiClient.create(), USER_ID)
    }

    val syncManager: SyncManager by lazy {
        SyncManager(db, api)
    }

    val repository: ProductRepository by lazy {
        ProductRepository(db, api, syncManager)
    }
}
