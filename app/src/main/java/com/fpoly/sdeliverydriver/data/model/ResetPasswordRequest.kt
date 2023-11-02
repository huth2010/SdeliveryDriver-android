package com.fpoly.sdeliverydriver.data.model

data class ResetPasswordRequest(
    val userId: String,
    val newPassword: String,
    val confirmPassword: String
)