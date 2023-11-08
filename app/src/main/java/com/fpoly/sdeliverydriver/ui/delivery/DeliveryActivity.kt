package com.fpoly.sdeliverydriver.ui.delivery

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.core.PolyBaseActivity
import com.fpoly.sdeliverydriver.databinding.ActivityDeliveryBinding
import com.fpoly.sdeliverydriver.databinding.ActivityMainBinding
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewModel
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewState
import com.fpoly.sdeliverydriver.ui.main.profile.UserViewModel
import com.fpoly.sdeliverydriver.ui.main.profile.UserViewState
import com.fpoly.sdeliverydriver.ui.security.SecurityViewModel
import javax.inject.Inject

class DeliveryActivity : PolyBaseActivity<ActivityDeliveryBinding>(),HomeViewModel.Factory,
     UserViewModel.Factory {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModel.Factory

    @Inject
    lateinit var userViewModelFactory: UserViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PolyApplication).polyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun getBinding(): ActivityDeliveryBinding {
        return ActivityDeliveryBinding.inflate(layoutInflater)
    }

    override fun create(initialState: HomeViewState): HomeViewModel {
        return homeViewModelFactory.create(initialState)
    }

    override fun create(initialState: UserViewState): UserViewModel {
        return userViewModelFactory.create(initialState)
    }
}