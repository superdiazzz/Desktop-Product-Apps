package com.zulhija_nanda.product.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponse(
    val data: List<ProductResponse>,
    val pagination: Pagination
)

@Serializable
data class Pagination(
    val page: Int,
    val limit: Int,
    val total: Int,
    val pages: Int
)