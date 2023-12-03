package com.fpoly.sdeliverydriver.ui.call

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.fpoly.sdeliverydriver.data.model.Message
import com.fpoly.sdeliverydriver.data.model.RequireCall
import com.fpoly.sdeliverydriver.data.model.Room
import com.fpoly.sdeliverydriver.data.model.User

data class CallViewState(
    val curentRoom: Async<Room> = Uninitialized,
    val curentMessage: Async<Message> = Uninitialized,

    var offerMessage: Async<Message> = Uninitialized,
    var answerMessage: Async<Message> = Uninitialized,

    var requireCall: RequireCall? = null,
    var requireCallIceCandidate: RequireCall? = null,
): MvRxState {
}