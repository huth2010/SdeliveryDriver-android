package com.fpoly.sdeliverydriver.ui.security

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.fpoly.sdeliverydriver.data.model.TokenResponse
import com.fpoly.sdeliverydriver.data.model.User
import com.fpoly.sdeliverydriver.data.model.VerifyOTPResponse

data class SecurityViewState (
    var asyncLogin: Async<TokenResponse> = Uninitialized,
    var asyncForgotPassword: Async<VerifyOTPResponse> = Uninitialized,
    var asyncUserCurrent: Async<User> = Uninitialized,
    var asyncResetPassword: Async<User> = Uninitialized
): MvRxState