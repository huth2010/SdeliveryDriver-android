package com.fpoly.sdeliverydriver.ui.main.profile

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.fpoly.sdeliverydriver.data.model.Address
import com.fpoly.sdeliverydriver.data.model.TokenResponse
import com.fpoly.sdeliverydriver.data.model.User

data class UserViewState(
    var asyncLogout: Async<User> = Uninitialized,
    var asyncCurrentUser: Async<User> = Uninitialized,
    var asyncUserSearching: Async<User> = Uninitialized,
    var asyncUpdateUser: Async<User> = Uninitialized,
    var asyncListAddress: Async<List<Address>> = Uninitialized,
    var asyncAddress: Async<Address> = Uninitialized,
    var asyncCreateAddress: Async<Address> = Uninitialized,
    var asyncDeleteAddress: Async<Address> = Uninitialized,
    var asyncChangePassword: Async<TokenResponse> = Uninitialized,
): MvRxState {

}