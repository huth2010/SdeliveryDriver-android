package com.fpoly.sdeliverydriver.ui.chat

import com.fpoly.sdeliverydriver.core.PolyViewAction
import com.fpoly.sdeliverydriver.data.model.Gallery
import com.fpoly.sdeliverydriver.data.model.Message
import com.fpoly.sdeliverydriver.data.model.Room
import com.fpoly.sdeliverydriver.data.model.User
import java.io.File

sealed class ChatViewAction : PolyViewAction{

    object getCurentUser : ChatViewAction()
    object getRoomChat : ChatViewAction()

    data class setCurrentUserChat(val user: User): ChatViewAction()
    data class setCurrentChat(val roomId: String?): ChatViewAction()
    object removeCurrentChat: ChatViewAction()

    data class postMessage(val message: Message, val images: List<Gallery>? = null, val pathPhoto: String? = null): ChatViewAction()
    object removePostMessage: ChatViewAction()

    object returnConnectSocket: ChatViewAction()
    object returnDisconnectSocket: ChatViewAction()
    data class returnOffEventMessageSocket(val roomId: String): ChatViewAction()

    object getDataGallery: ChatViewAction()

    data class searchUserByName(val text: String): ChatViewAction()
    data class findRoomSearch(val userId: String?): ChatViewAction()
}