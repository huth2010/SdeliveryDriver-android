package com.fpoly.sdeliverydriver.ui.main.home

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.fpoly.sdeliverydriver.data.model.User

data class HomeViewState(
    var users : Async<List<User>> = Uninitialized,
): MvRxState {
}