package com.fpoly.sdeliverydriver.ui.security

import com.fpoly.sdeliverydriver.core.PolyViewEvent

sealed class SecurityViewEvent:PolyViewEvent {
    object ReturnLoginEvent:SecurityViewEvent()
    object ReturnVerifyOTPEvent:SecurityViewEvent()
    object ReturnResetPassEvent:SecurityViewEvent()
    object ReturnForgotPassEvent:SecurityViewEvent()
}