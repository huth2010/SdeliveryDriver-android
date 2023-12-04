package com.fpoly.sdeliverydriver.ui.main.home

import com.fpoly.sdeliverydriver.core.PolyViewAction
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest

sealed class HomeViewAction : PolyViewAction {
    data class GetAllOrderByStatus(val statusId: String) : HomeViewAction()
    data class GetAllDeliveryOrders(val statusId: String) : HomeViewAction()
    data class GetCurrentLocation(val lat: Double,val lon: Double) : HomeViewAction()
    data class GetCurrentOrder(val id: String) : HomeViewAction()
    data class UpdateOrderStatus(val id: String,val shipperId: String, val statusRequest: UpdateStatusRequest): HomeViewAction()
    object getDataGallery: HomeViewAction()
}