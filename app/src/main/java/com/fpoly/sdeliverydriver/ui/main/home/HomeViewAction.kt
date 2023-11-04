package com.fpoly.sdeliverydriver.ui.main.home

import com.fpoly.sdeliverydriver.core.PolyViewAction
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest

sealed class HomeViewAction : PolyViewAction {
    data class GetAllOrderByStatus(val statusId: String) : HomeViewAction()
    data class UpdateOrderStatus(val id: String, val statusRequest: UpdateStatusRequest): HomeViewAction()
}