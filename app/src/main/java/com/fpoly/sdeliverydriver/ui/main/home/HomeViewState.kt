package com.fpoly.sdeliverydriver.ui.main.home

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.data.model.User

data class HomeViewState(
    var users : Async<List<User>> = Uninitialized,
    var asyncUnconfirmed: Async<List<OrderResponse>> = Uninitialized,
    var asyncConfirmed: Async<List<OrderResponse>> = Uninitialized,
    var asyncDelivering: Async<List<OrderResponse>> = Uninitialized,
    var asyncCancelled: Async<List<OrderResponse>> = Uninitialized,
    var asyncCompleted: Async<List<OrderResponse>> = Uninitialized,
    var asyncUpdateOrderStatus: Async<OrderResponse> = Uninitialized,
): MvRxState {
    var isSwipeLoading = asyncUnconfirmed is Loading
}