package com.fpoly.sdeliverydriver.ui.call

import com.fpoly.sdeliverydriver.core.PolyViewAction
import com.fpoly.sdeliverydriver.data.model.Gallery
import com.fpoly.sdeliverydriver.data.model.Message
import com.fpoly.sdeliverydriver.ui.chat.ChatViewAction

sealed class CallViewAction: PolyViewAction {
    data class GetCurrentRoom(val userId: String?): CallViewAction()
    data class GetLastCallMessage(val roomId: String?): CallViewAction()
    data class postMessage(val message: Message): CallViewAction()
}