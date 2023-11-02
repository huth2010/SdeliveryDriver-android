package com.fpoly.sdeliverydriver.core.example

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.fpoly.sdeliverydriver.data.model.Gallery
import com.fpoly.sdeliverydriver.data.model.Message
import com.fpoly.sdeliverydriver.data.model.RequireCall
import com.fpoly.sdeliverydriver.data.model.Room
import com.fpoly.sdeliverydriver.data.model.User

data class ChatViewState(
    val curentUser: Async<User> = Uninitialized,
    val rooms: Async<ArrayList<Room>> = Uninitialized,

    val curentRoom: Async<Room> = Uninitialized,
    val curentMessage: Async<ArrayList<Message>> = Uninitialized,
    val messageSent: Async<Message> = Uninitialized,
    val galleries: Async<ArrayList<Gallery>> = Uninitialized,

    val newMessage: Async<Message> = Uninitialized,                    // Message được socket giử về

    val curentUsersSreach: Async<ArrayList<User>> = Uninitialized,

    val requireCall: RequireCall? = null,
    val requireCallIceCandidate: RequireCall? = null,
    val curentCallWithUser: User? = null,
): MvRxState

