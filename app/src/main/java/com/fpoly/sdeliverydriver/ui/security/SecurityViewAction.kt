package com.fpoly.sdeliverydriver.ui.security

import com.fpoly.sdeliverydriver.core.PolyViewAction
import com.fpoly.sdeliverydriver.data.model.Data
import com.fpoly.sdeliverydriver.data.model.ResetPasswordRequest
import com.fpoly.sdeliverydriver.data.model.TokenResponse
import com.fpoly.sdeliverydriver.data.model.UserRequest
import com.fpoly.sdeliverydriver.data.model.VerifyOTPRequest

sealed class SecurityViewAction : PolyViewAction {
    data class LoginAction(val userName: String, var password: String) : SecurityViewAction()
    data class SaveTokenAction(val token: TokenResponse) : SecurityViewAction()
    data class VerifyOTPResetPassAction(val verifyOTPRequest: VerifyOTPRequest) : SecurityViewAction()
    data class ResendResetPassOTPCode(val resendOTPCode: Data): SecurityViewAction()
    data class ForgotPassword(val email: String): SecurityViewAction()
    data class ResetPassword(val resetPasswordRequest: ResetPasswordRequest):SecurityViewAction()

}