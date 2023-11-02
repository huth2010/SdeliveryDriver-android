package com.fpoly.sdeliverydriver.data.model

import java.io.Serializable

data class User(
    var _id: String="",
    var avatar: Image? =null,
    var authType: String="",
    var email: String="",
    var birthday: String="",
    var gender: Boolean,
    var facebookId: String="",
    var first_name: String="",
    var googleId: String="",
    var last_name: String="",
    var password: String="",
    var phone: String="",
    var role: String="",
    var verified: Boolean=false,
    var confirmPassword: String=""
): Serializable

