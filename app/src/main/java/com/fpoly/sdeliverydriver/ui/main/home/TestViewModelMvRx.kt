package com.fpoly.sdeliverydriver.ui.main.home

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.fpoly.sdeliverydriver.core.PolyBaseViewModel
import com.fpoly.sdeliverydriver.data.network.RemoteDataSource
import com.fpoly.sdeliverydriver.data.network.UserApi
import com.fpoly.sdeliverydriver.data.repository.TestRepo

class TestViewModelMvRx(state: HomeViewState, private val repo: TestRepo)
    : PolyBaseViewModel<HomeViewState, HomeViewAction, HomeViewEvent>(state) {

    override fun handle(action: HomeViewAction) {
    }

    companion object : MvRxViewModelFactory<TestViewModelMvRx, HomeViewState> {
        override fun create(viewModelContext: ViewModelContext, state: HomeViewState): TestViewModelMvRx? {
            var repo : TestRepo = TestRepo(RemoteDataSource().buildApi(UserApi::class.java, viewModelContext.activity.applicationContext))
            return TestViewModelMvRx(state, repo)
        }
    }
}