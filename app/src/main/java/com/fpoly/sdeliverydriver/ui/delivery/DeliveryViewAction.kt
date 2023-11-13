package com.fpoly.sdeliverydriver.ui.delivery

import com.fpoly.sdeliverydriver.core.PolyViewAction
import com.fpoly.sdeliverydriver.data.model.CreateDeliveryOrderRequest
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewAction

sealed class DeliveryViewAction: PolyViewAction {
    data class GetAllOrderByStatus(val statusId: String) : DeliveryViewAction()
    data class SetCurrentCancelReason(val cancelReason: String) : DeliveryViewAction()
    data class CreateDeliveryOrder(val request: CreateDeliveryOrderRequest) : DeliveryViewAction()
    data class GetCurrentOrder(val id: String) : DeliveryViewAction()
    data class UpdateOrderStatus(val id: String,val shipperId: String, val statusRequest: UpdateStatusRequest): DeliveryViewAction()
}