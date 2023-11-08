package com.fpoly.sdeliverydriver.ui.main.home

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.fpoly.sdeliverydriver.core.PolyBaseViewModel
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest
import com.fpoly.sdeliverydriver.data.model.UserLocation
import com.fpoly.sdeliverydriver.data.repository.OrderRepository
import com.fpoly.sdeliverydriver.data.repository.PlacesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HomeViewModel @AssistedInject constructor(
    @Assisted state: HomeViewState,
    private val repository: OrderRepository,
    private val placesRepository: PlacesRepository
) : PolyBaseViewModel<HomeViewState, HomeViewAction, HomeViewEvent>(state) {

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
        repository.getCurrentOrder(id)
            .execute {
                copy(asyncGetCurrentOrder = it)
            }
    }

    private fun handleUpdateOrderStatus(id: String, shipperId: String,statusRequest: UpdateStatusRequest) {
        setState { copy(asyncUpdateOrderStatus = Loading()) }
        repository.updateOrderStatus(id, shipperId ,statusRequest)
            .execute {
                copy(asyncUpdateOrderStatus = it)
            }
    }

    private fun handleGetAllOrderByStatus(statusId: String) {
        when (statusId) {
            "65264c102d9b3bb388078976" -> {
                setState { copy(asyncConfirmed = Loading()) }
                repository.getAllOrderByStatus(statusId)
                    .execute {
                        copy(asyncConfirmed = it)
                    }
            }

            "65264c672d9b3bb388078978" -> {
                setState { copy(asyncDelivering = Loading()) }
                repository.getAllOrderByShipper(statusId)
                    .execute {
                        copy(asyncDelivering = it)
                    }
            }

            "6526a6e6adce6a54f6f67d7d" -> {
                setState { copy(asyncCompleted = Loading()) }
                repository.getAllOrderByShipper(statusId)
                    .execute {
                        copy(asyncCompleted = it)
                    }
            }

            "653bc0a72006e5791beab35b" -> {
                setState { copy(asyncCancelled = Loading()) }
                repository.getAllOrderByShipper(statusId)
                    .execute {
                        copy(asyncCancelled = it)
                    }
            }
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

