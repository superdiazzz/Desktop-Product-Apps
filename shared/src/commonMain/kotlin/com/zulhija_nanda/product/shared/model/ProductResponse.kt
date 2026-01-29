package com.zulhija_nanda.product.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val id: Int,
    val title: String ?= "",
    val description: String ? = "",
    val userId: String,
    val createdAt: String,
    val updatedAt: String
)
