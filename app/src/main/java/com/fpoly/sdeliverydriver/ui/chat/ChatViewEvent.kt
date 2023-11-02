package com.fpoly.sdeliverydriver.ui.chat

import com.fpoly.sdeliverydriver.core.PolyViewEvent
import org.webrtc.MediaStream

sealed class ChatViewEvent : PolyViewEvent{
    object initObserverPeerConnection: ChatViewEvent()
    data class addViewToViewWebRTC(val p0: MediaStream?): ChatViewEvent()
}