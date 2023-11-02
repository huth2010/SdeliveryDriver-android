package com.fpoly.sdeliverydriver.core

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.fpoly.sdeliverydriver.ultis.DataSource
import com.fpoly.sdeliverydriver.ultis.PublishDataSource

//import com.fpoly.sdeliverydriver.ultis.DataSource
//import com.fpoly.sdeliverydriver.ultis.PublishDataSource

abstract class PolyBaseViewModel<S: MvRxState, VA: PolyViewAction, VE: PolyViewEvent>(state: S)
    : BaseMvRxViewModel<S>(state, false) {

//    interface Factory<S : MvRxState> {
//        fun create(state: S): BaseMvRxViewModel<S>
//    }

    protected val _viewEvents = PublishDataSource<VE>()
    val viewEvents: DataSource<VE> = _viewEvents

    abstract fun handle(action: VA)
}
