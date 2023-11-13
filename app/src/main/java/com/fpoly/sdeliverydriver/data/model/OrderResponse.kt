package com.fpoly.sdeliverydriver.data.model

import java.io.Serializable

data class OrderResponse(
    val _id: String,
    val userId: User,
    val couponId: String,
    val products: List<ProductCart>,
    val discount: Int,
    val total: Int,
    val status: Status,
    val address: Address,
    val notes: String,
    val shipperId: String,
    var statusPayment: Status,
    var isPayment: Boolean,
    val createdAt: String,
    val updatedAt: String,
):Serializable