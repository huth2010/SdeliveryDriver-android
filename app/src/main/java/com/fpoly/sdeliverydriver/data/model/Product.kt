package com.fpoly.sdeliverydriver.data.model

data class Product(
    val _id: String,
    val categoryId: String,
    val createdAt: String,
    val images: List<Image>,
    val description: String,
    val product_name: String,
    val product_price: Int,
    val sold_quantity: Int,
    val updatedAt: String,
    val views: Int,
    val isActive: Boolean,
    val deleted: Boolean,
)