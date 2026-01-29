package com.zulhija_nanda.product.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductRequest(
    val title: String,
    val description: String
)
