package com.fpoly.sdeliverydriver.data.repository

import com.fpoly.sdeliverydriver.data.model.Data
import com.fpoly.sdeliverydriver.data.model.LoginRequest
import com.fpoly.sdeliverydriver.data.model.ResetPasswordRequest
import com.fpoly.sdeliverydriver.data.model.TokenDevice
import com.fpoly.sdeliverydriver.data.model.TokenResponse
import com.fpoly.sdeliverydriver.data.model.User
import com.fpoly.sdeliverydriver.data.model.UserRequest
import com.fpoly.sdeliverydriver.data.model.VerifyOTPRequest
import com.fpoly.sdeliverydriver.data.model.VerifyOTPResponse
import com.fpoly.sdeliverydriver.data.network.AuthApi
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class AuthRepository(
    private val api: AuthApi,
) {
    fun login(email: String, password: String): Observable<TokenResponse> = api.login(
        LoginRequest(email,password)
    ).subscribeOn(Schedulers.io())

    fun verifyOTPChangePassword(verifyOTP: VerifyOTPRequest): Observable<User> =
        api.verifyOTPChangePassword(verifyOTP).subscribeOn(Schedulers.io())

    fun resendOTPCode(resendOTPCode: Data): Observable<VerifyOTPResponse> =
        api.resendOTPCode(resendOTPCode).subscribeOn(Schedulers.io())

    fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Observable<User> =
        api.resetPassword(resetPasswordRequest).subscribeOn(Schedulers.io())

    fun forgotPassword(email: String): Observable<VerifyOTPResponse> =
        api.forgotPassword(email).subscribeOn(Schedulers.io())

    fun getCurrentUser(): Observable<User> = api.getCurrentUser().subscribeOn(Schedulers.io())

    fun getTokenDevice(callBack: (tokenDeivce: String) -> Unit){
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            callBack(it.result)
        }
    }

    fun addTokenDevice(tokenDevice: TokenDevice): Observable<User> =  api.updateTokenDevice(tokenDevice).subscribeOn(Schedulers.io())

}