package com.fpoly.sdeliverydriver.ui.delivery

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.fpoly.sdeliverydriver.core.PolyBaseViewModel
import com.fpoly.sdeliverydriver.data.model.CreateDeliveryOrderRequest
import com.fpoly.sdeliverydriver.data.model.Data
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest
import com.fpoly.sdeliverydriver.data.repository.DeliveryRepository
import com.fpoly.sdeliverydriver.data.repository.OrderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DeliveryViewModel @AssistedInject constructor(
    @Assisted state: DeliveryViewState,
    private val orderRepository: OrderRepository,
    private val deliveryRepository: DeliveryRepository
) : PolyBaseViewModel<DeliveryViewState, DeliveryViewAction, DeliveryViewEvent>(state) {

    override fun handle(action: DeliveryViewAction) {
        when (action) {
            is DeliveryViewAction.GetAllOrderByStatus -> handleGetAllOrderByStatus(action.statusId)
            is DeliveryViewAction.UpdateOrderStatus -> handleUpdateOrderStatus(
                action.id,
                action.shipperId,
                action.statusRequest
            )
            is DeliveryViewAction.GetCurrentOrder -> handleGetCurrentOrder(action.id)
            is DeliveryViewAction.CreateDeliveryOrder -> handleCreateDeliveryOrder(action.request)
            is DeliveryViewAction.SetCurrentCancelReason -> handleSetCurrentCancelReason(action.cancelReason)
            else -> {}
        }
    }

    private fun handleCreateDeliveryOrder(request: CreateDeliveryOrderRequest) {
        setState { copy(asyncCreateDelivery = Loading()) }
        deliveryRepository.createDeliveryOrder(request)
            .execute {
                copy(asyncCreateDelivery = it)
            }
    }

    private fun handleGetCurrentOrder(id: String) {
        setState { copy(asyncGetCurrentOrder = Loading()) }
        orderRepository.getCurrentOrder(id)
            .execute {
                copy(asyncGetCurrentOrder = it)
            }
    }

    private fun handleUpdateOrderStatus(id: String, shipperId: String,statusRequest: UpdateStatusRequest) {
        setState { copy(asyncUpdateOrderStatus = Loading()) }
        orderRepository.updateOrderStatus(id, shipperId ,statusRequest)
            .execute {
                copy(asyncUpdateOrderStatus = it)
            }
    }

    private fun handleGetAllOrderByStatus(statusId: String) {
        when (statusId) {

            "65264c672d9b3bb388078978" -> {
                setState { copy(asyncDelivering = Loading()) }
                orderRepository.getAllOrderByShipper(statusId)
                    .execute {
                        copy(asyncDelivering = it)
                    }
            }

            "6526a6e6adce6a54f6f67d7d" -> {
                setState { copy(asyncCompleted = Loading()) }
                orderRepository.getAllOrderByShipper(statusId)
                    .execute {
                        copy(asyncCompleted = it)
                    }
            }

            "653bc0a72006e5791beab35b" -> {
                setState { copy(asyncCancelled = Loading()) }
                orderRepository.getAllOrderByShipper(statusId)
                    .execute {
                        copy(asyncCancelled = it)
                    }
            }
        }
    }

    private fun handleSetCurrentCancelReason(cancelReason: String) {
        setState { copy(cancelReason= cancelReason) }
    }
    fun handleRemoveAsyncCreateDelivery() {
        setState { copy(asyncCreateDelivery= Uninitialized) }
    }

    fun sendData(data: String) {
        _viewEvents.post(DeliveryViewEvent.CancelReason(data))
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: DeliveryViewState): DeliveryViewModel
    }


    companion object : MvRxViewModelFactory<DeliveryViewModel, DeliveryViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: DeliveryViewState
        ): DeliveryViewModel? {
            val factory = when (viewModelContext) {
                is FragmentViewModelContext -> viewModelContext.fragment as? Factory
                is ActivityViewModelContext -> viewModelContext.activity as? Factory
            }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}

