package com.fpoly.sdeliverydriver.data.model

data class AddressRequest(
    val recipientName: String,
    val phoneNumber: String,
    val addressLine: String,
    val latitude: Double,
    val longitude: Double
)