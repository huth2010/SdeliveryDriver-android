package com.fpoly.sdeliverydriver.ui.delivery

import android.os.Bundle
import com.airbnb.mvrx.viewModel
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.core.PolyBaseActivity
import com.fpoly.sdeliverydriver.databinding.ActivityDeliveryBinding
import com.fpoly.sdeliverydriver.ui.main.profile.UserViewModel
import com.fpoly.sdeliverydriver.ui.main.profile.UserViewState
import javax.inject.Inject

class DeliveryActivity : PolyBaseActivity<ActivityDeliveryBinding>(), DeliveryViewModel.Factory,
    UserViewModel.Factory {
    private val deliveryViewModel: DeliveryViewModel by viewModel()

    @Inject
    lateinit var deliveryViewModelFactory: DeliveryViewModel.Factory

    @Inject
    lateinit var userViewModelFactory: UserViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PolyApplication).polyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        deliveryViewModel.observeViewEvents {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: DeliveryViewEvent?) {
        when (event) {

            else -> {}
        }
    }

    override fun getBinding(): ActivityDeliveryBinding {
        return ActivityDeliveryBinding.inflate(layoutInflater)
    }

    override fun create(initialState: DeliveryViewState): DeliveryViewModel {
        return deliveryViewModelFactory.create(initialState)
    }

    override fun create(initialState: UserViewState): UserViewModel {
        return userViewModelFactory.create(initialState)
    }
}