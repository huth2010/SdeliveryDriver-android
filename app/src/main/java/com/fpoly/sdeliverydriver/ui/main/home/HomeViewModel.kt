package com.fpoly.sdeliverydriver.ui.main.home

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.fpoly.sdeliverydriver.core.PolyBaseViewModel
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest
import com.fpoly.sdeliverydriver.data.repository.OrderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HomeViewModel @AssistedInject constructor(
    @Assisted state: HomeViewState,
    private val repository: OrderRepository
) : PolyBaseViewModel<HomeViewState, HomeViewAction, HomeViewEvent>(state) {

    override fun handle(action: HomeViewAction) {
        when (action) {
            is HomeViewAction.GetAllOrderByStatus -> handleGetAllOrderByStatus(action.statusId)
            is HomeViewAction.UpdateOrderStatus -> handleUpdateOrderStatus(
                action.id,
                action.statusRequest
            )
        }
    }

    private fun handleUpdateOrderStatus(id: String, statusRequest: UpdateStatusRequest) {
        setState { copy(asyncUpdateOrderStatus = Loading()) }
        repository.updateOrderStatus(id, statusRequest)
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
                repository.getAllOrderByStatus(statusId)
                    .execute {
                        copy(asyncDelivering = it)
                    }
            }

            "6526a6e6adce6a54f6f67d7d" -> {
                setState { copy(asyncCompleted = Loading()) }
                repository.getAllOrderByStatus(statusId)
                    .execute {
                        copy(asyncCompleted = it)
                    }
            }

            "653bc0a72006e5791beab35b" -> {
                setState { copy(asyncCancelled = Loading()) }
                repository.getAllOrderByStatus(statusId)
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

