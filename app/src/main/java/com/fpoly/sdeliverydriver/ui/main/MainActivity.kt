package com.fpoly.sdeliverydriver.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.mvrx.viewModel
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseActivity
import com.fpoly.sdeliverydriver.data.network.SessionManager
import com.fpoly.sdeliverydriver.databinding.ActivityMainBinding
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewEvent
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewModel
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewState
import com.fpoly.sdeliverydriver.ui.main.home.TestViewModel
import com.fpoly.sdeliverydriver.ui.main.home.TestViewModelMvRx
import com.fpoly.sdeliverydriver.ui.main.profile.UserViewModel
import com.fpoly.sdeliverydriver.ui.main.profile.UserViewState
import com.fpoly.sdeliverydriver.ui.security.SecurityViewModel
import com.fpoly.sdeliverydriver.ui.security.SecurityViewState
import com.fpoly.sdeliverydriver.ultis.addFragmentToBackstack
import com.fpoly.sdeliverydriver.ultis.changeLanguage
import com.fpoly.sdeliverydriver.ultis.changeMode
import javax.inject.Inject


class MainActivity : PolyBaseActivity<ActivityMainBinding>(), HomeViewModel.Factory,
    SecurityViewModel.Factory, UserViewModel.Factory {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModel.Factory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var securityModelFactory: SecurityViewModel.Factory

    @Inject
    lateinit var userViewModelFactory: UserViewModel.Factory

    @Inject
    lateinit var sessionManager: SessionManager

    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PolyApplication).polyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setupBottomNavigation()
        handleViewModel()
        changeMode(sessionManager.fetchDarkMode())
        changeLanguage(sessionManager.fetchLanguage())
    }

    private fun handleViewModel() {
        homeViewModel.observeViewEvents {
            if (it != null) {
                handleEvent(it)
            }
        }
    }

    override fun getBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private fun setupBottomNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        views.bottomNav.setupWithNavController(navController)
    }

    private fun handleEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.ReturnVisibleBottomNav -> visibilityBottomNav(event.isVisibleBottomNav)
            is HomeViewEvent.ChangeDarkMode -> handleDarkMode(event.isCheckedDarkMode)
        }
    }

    private fun handleDarkMode(checkedDarkMode: Boolean) {
        sessionManager.saveDarkMode(checkedDarkMode)
        changeMode(checkedDarkMode)
        changeLanguage(sessionManager.fetchLanguage())
    }

    fun visibilityBottomNav(isVisible: Boolean) {
        views.bottomNav.isVisible = isVisible
    }

    override fun create(initialState: HomeViewState): HomeViewModel {
        return homeViewModelFactory.create(initialState)
    }


    override fun create(initialState: SecurityViewState): SecurityViewModel {
        return securityModelFactory.create(initialState)
    }

    override fun create(initialState: UserViewState): UserViewModel {
        return userViewModelFactory.create(initialState)
    }


}