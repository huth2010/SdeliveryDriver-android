package com.fpoly.sdeliverydriver.ui.delivery

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.fpoly.sdeliverydriver.data.model.DeliveryOrder
import com.fpoly.sdeliverydriver.data.model.OpenStreetMapResponse
import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.data.model.User

data class DeliveryViewState(
    var users: Async<List<User>> = Uninitialized,
    var asyncDelivering: Async<List<OrderResponse>> = Uninitialized,
    var asyncCancelled: Async<List<OrderResponse>> = Uninitialized,
    var asyncCompleted: Async<List<OrderResponse>> = Uninitialized,
    var asyncUpdateOrderStatus: Async<OrderResponse> = Uninitialized,
    var asyncGetCurrentOrder: Async<OrderResponse> = Uninitialized,
    var asyncCreateDelivery: Async<DeliveryOrder> = Uninitialized,
    var cancelReason: String? = null,
) : MvRxState {
    var isSwipeLoading = asyncDelivering is Loading
}