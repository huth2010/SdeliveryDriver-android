package com.fpoly.sdeliverydriver.data.network

import com.fpoly.sdeliverydriver.data.model.Data
import com.fpoly.sdeliverydriver.data.model.LoginRequest
import com.fpoly.sdeliverydriver.data.model.ResetPasswordRequest
import com.fpoly.sdeliverydriver.data.model.TokenDevice
import com.fpoly.sdeliverydriver.data.model.TokenResponse
import com.fpoly.sdeliverydriver.data.model.User
import com.fpoly.sdeliverydriver.data.model.UserRequest
import com.fpoly.sdeliverydriver.data.model.VerifyOTPRequest
import com.fpoly.sdeliverydriver.data.model.VerifyOTPResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
//    @POST("oauth/token")
//    fun loginWithRefreshToken(@Body credentials: UserCredentials): Call<TokenResponse>
    @POST("api/signinDeliveryApp")
    fun login(@Body user: LoginRequest): Observable<TokenResponse>
    @POST("api/verifyOTPChangePassword")
    fun verifyOTPChangePassword(@Body verifyOTP: VerifyOTPRequest):Observable<User>
    @POST("api/resendOTPVerificationCode")
    fun resendOTPCode(@Body resendOTPCode: Data):Observable<VerifyOTPResponse>
    @GET("api/forgotPassword")
    fun forgotPassword(@Query("email") email: String):Observable<VerifyOTPResponse>
    @POST("api/resetPassword")
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest):Observable<User>
    @GET("api/getCurrentUser")
    fun getCurrentUser():Observable<User>
    @GET("api/users/{id}")
    fun getUserById(@Path("id") id: String):Observable<User>
    @GET("api/users/search/{text}")
    fun searchUserByName(@Path("text") text: String):Observable<ArrayList<User>>
    @POST("api/update/tokendevice")
    fun updateTokenDevice(@Body tokenDevice: TokenDevice):Observable<User>
}