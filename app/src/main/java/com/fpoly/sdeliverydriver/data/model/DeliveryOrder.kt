package com.fpoly.sdeliverydriver.data.model

data class DeliveryOrder(
    val shipperId: String,
    val orderCode: String,
    val status: Status,
    val image: Image,
    val comment: String,
    val cancelReason: String
)