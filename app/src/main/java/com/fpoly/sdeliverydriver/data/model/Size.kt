package com.fpoly.sdeliverydriver.data.model

data class Size (
    val _id: String,
    val size_name: String,
    val size_price: Int
){

    var isSelected: Boolean = false
}