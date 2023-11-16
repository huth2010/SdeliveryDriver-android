package com.fpoly.sdeliverydriver.data.repository

import com.fpoly.sdeliverydriver.data.model.Address
import com.fpoly.sdeliverydriver.data.model.AddressRequest
import com.fpoly.sdeliverydriver.data.model.ChangePassword
import com.fpoly.sdeliverydriver.data.model.TokenResponse
import com.fpoly.sdeliverydriver.data.model.UpdateUserRequest
import com.fpoly.sdeliverydriver.data.model.User
import com.fpoly.sdeliverydriver.data.network.UserApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class UserRepository(
    private val api: UserApi
) {
    fun changePassword(changePassword: ChangePassword): Observable<TokenResponse> =
        api.changePassword(changePassword).subscribeOn(Schedulers.io())

    fun getCurrentUser(): Observable<User> = api.getCurrentUser().subscribeOn(Schedulers.io())

    fun getUserById(id: String): Observable<User> = api.getUserById(id).subscribeOn(Schedulers.io())

    fun updateUser(updateUserRequest: UpdateUserRequest): Observable<User> =
        api.updateUser(updateUserRequest).subscribeOn(Schedulers.io())

    fun uploadAvatar(avatar: MultipartBody.Part): Observable<User> =
        api.uploadAvatar(avatar).subscribeOn(Schedulers.io())

    fun getAllAddressByUser(): Observable<List<Address>> =
        api.getAllAddressByUser().subscribeOn(Schedulers.io())

    fun createAddress(addressRequest: AddressRequest): Observable<Address> =
        api.createAddress(addressRequest).subscribeOn(Schedulers.io())

    fun deleteAddress(id: String): Observable<Address> =
        api.deleteAddress(id).subscribeOn(Schedulers.io())
    fun getAddressById(id: String): Observable<Address> =
        api.getAddressById(id).subscribeOn(Schedulers.io())

    fun logout(): Observable<User> =  api.logoutUser().subscribeOn(Schedulers.io())
}