package com.zulhija_nanda.product.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val localId: Long? = null,
    val serverId: Int? = null,
    val title: String ?= null,
    val description: String ?= null,
    val userId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
