package com.fpoly.sdeliverydriver.data.model

data class VerifyOTPRequest(
    val userId: String?,
    val otp:String
) {
}