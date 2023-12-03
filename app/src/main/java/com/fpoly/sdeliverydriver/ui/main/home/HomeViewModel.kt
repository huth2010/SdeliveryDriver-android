package com.fpoly.sdeliverydriver.ui.main.home

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.fpoly.sdeliverydriver.core.PolyBaseViewModel
import com.fpoly.sdeliverydriver.data.model.DeliveryOrder
import com.fpoly.sdeliverydriver.data.model.TokenDevice
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest
import com.fpoly.sdeliverydriver.data.model.UserLocation
import com.fpoly.sdeliverydriver.data.repository.AuthRepository
import com.fpoly.sdeliverydriver.data.repository.DeliveryRepository
import com.fpoly.sdeliverydriver.data.repository.HomeRepository
import com.fpoly.sdeliverydriver.data.repository.OrderRepository
import com.fpoly.sdeliverydriver.data.repository.PlacesRepository
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.CANCEL_STATUS
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.CONFIRMED_STATUS
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.DELIVERING_STATUS
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.SUCCESS_STATUS
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel @AssistedInject constructor(
    @Assisted state: HomeViewState,
    private val authRepository: AuthRepository,
    private val repo: HomeRepository,
    private val orderRepository: OrderRepository,
    private val placesRepository: PlacesRepository,
    private val deliveryOrderRepository: DeliveryRepository
) : PolyBaseViewModel<HomeViewState, HomeViewAction, HomeViewEvent>(state) {

    init {
        handleGetAllOrderByStatus(CONFIRMED_STATUS)
        handleGetAllOrderByStatus(DELIVERING_STATUS)
        handleGetAllDeliveryOrders(SUCCESS_STATUS)
        handleGetAllDeliveryOrders(CANCEL_STATUS)

        authRepository.getTokenDevice{
            authRepository.addTokenDevice(TokenDevice(it)).execute {
                copy()
            }
        }
    }

    override fun handle(action: HomeViewAction) {
        when (action) {
            is HomeViewAction.GetAllOrderByStatus -> handleGetAllOrderByStatus(action.statusId)
            is HomeViewAction.UpdateOrderStatus -> handleUpdateOrderStatus(
                action.id,
                action.shipperId,
                action.statusRequest
            )
            is HomeViewAction.GetCurrentOrder -> handleGetCurrentOrder(action.id)
            is HomeViewAction.GetCurrentLocation -> handleGetCurrentLocation(action.lat,action.lon)
            is HomeViewAction.GetAllDeliveryOrders -> handleGetAllDeliveryOrders(action.statusId)
            is HomeViewAction.getDataGallery -> getDataGallery()
        }
    }

    private fun handleGetAllDeliveryOrders(statusId: String) {
        when(statusId){
            SUCCESS_STATUS -> {
                setState { copy(asyncSuccessDeliveries = Loading()) }
                deliveryOrderRepository.getAllDeliveryOrders(statusId)
                    .execute {
                        copy(asyncSuccessDeliveries = it)
                    }
            }
            CANCEL_STATUS -> {
                setState { copy(asyncCancelDeliveries = Loading()) }
                deliveryOrderRepository.getAllDeliveryOrders(statusId)
                    .execute {
                        copy(asyncCancelDeliveries = it)
                    }
            }
        }

    }
    private fun handleGetCurrentLocation(lat: Double, lon: Double) {
        setState { copy(asyncGetCurrentLocation = Loading()) }
        placesRepository.getLocationName(lat,lon)
            .execute {
                copy(asyncGetCurrentLocation = it)
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
            CONFIRMED_STATUS -> {
                setState { copy(asyncConfirmed = Loading()) }
                orderRepository.getAllOrderByStatus(statusId)
                    .execute {
                        copy(asyncConfirmed = it)
                    }
            }

            DELIVERING_STATUS -> {
                setState { copy(asyncDelivering = Loading()) }
                orderRepository.getAllOrderByShipper(statusId)
                    .execute {
                        copy(asyncDelivering = it)
                    }
            }
        }
    }
    private fun getDataGallery() {
        setState { copy(galleries = Loading()) }
        CoroutineScope(Dispatchers.Main).launch{
            val data = repo.getDataFromGallery()
            val sortData= data.sortedByDescending { it.date }.toCollection(ArrayList())
            setState { copy(galleries =  Success(sortData)) }
        }
    }

    fun handleRemoveAsyncUpdateOrderStatus() {
        setState { copy(asyncUpdateOrderStatus=Uninitialized) }
    }
    fun handleChangeThemeMode(isChecked: Boolean) {
        _viewEvents.post(HomeViewEvent.ChangeDarkMode(isChecked))
    }

    fun returnVisibleBottomNav(isVisible: Boolean) {
        _viewEvents.post(HomeViewEvent.ReturnVisibleBottomNav(isVisible))
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: HomeViewState): HomeViewModel
    }


    companion object : MvRxViewModelFactory<HomeViewModel, HomeViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: HomeViewState
        ): HomeViewModel? {
            val fatory = when (viewModelContext) {
                is FragmentViewModelContext -> viewModelContext.fragment as? Factory
                is ActivityViewModelContext -> viewModelContext.activity as? Factory
            }
            return fatory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}

