package com.fpoly.sdeliverydriver.ui.call

import com.fpoly.sdeliverydriver.core.PolyViewEvent
import com.fpoly.sdeliverydriver.ui.chat.ChatViewEvent
import org.webrtc.MediaStream

sealed class CallViewEvent: PolyViewEvent {
    object initObserverPeerConnection: CallViewEvent()
    data class addViewToViewWebRTC(val p0: MediaStream?): CallViewEvent()
}