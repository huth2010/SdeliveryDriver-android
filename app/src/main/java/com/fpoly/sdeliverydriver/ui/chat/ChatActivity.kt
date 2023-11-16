package com.fpoly.sdeliverydriver.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseActivity
import com.fpoly.sdeliverydriver.core.example.ChatViewState
import com.fpoly.sdeliverydriver.data.model.IceCandidateModel
import com.fpoly.sdeliverydriver.data.model.RequireCall
import com.fpoly.sdeliverydriver.data.model.RequireCallType
import com.fpoly.sdeliverydriver.databinding.ActivityChatBinding
import com.fpoly.sdeliverydriver.ui.chat.call.MyPeerConnectionObserver
import com.fpoly.sdeliverydriver.ultis.MyConfigNotifi
import com.google.gson.Gson
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import javax.inject.Inject

class ChatActivity : PolyBaseActivity<ActivityChatBinding>(), ChatViewmodel.Factory{

    private lateinit var navController: NavController
    private val chatViewmodel: ChatViewmodel by viewModel()

    @Inject
    lateinit var chatViewmodelFactory: ChatViewmodel.Factory

    override fun getBinding(): ActivityChatBinding = ActivityChatBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PolyApplication).polyComponent.inject(this)
        super.onCreate(savedInstanceState)

        initUI()
        lisstenClickUI()
        handleViewMolde()
        handleReciveDataNotifi()
    }
    private fun initUI() {
        navController= findNavController(R.id.nav_fragment)

        chatViewmodel.initObserverPeerConnection(object: MyPeerConnectionObserver(){
            override fun onIceCandidate(p0: IceCandidate?) {
                super.onIceCandidate(p0)

                chatViewmodel.addIceCandidate(p0)
                val candidate = hashMapOf(
                    "sdpMid" to p0?.sdpMid,
                    "sdpMLineIndex" to p0?.sdpMLineIndex,
                    "sdpCandidate" to p0?.sdp
                )

                chatViewmodel.sendCallToServer(RequireCall(RequireCallType.ICE_CANDIDATE, null, null, candidate))
            }

            override fun onAddStream(p0: MediaStream?) {
                super.onAddStream(p0)
                chatViewmodel.addViewToViewWebRTC(p0)
            }
        })
    }

    private fun handleReciveDataNotifi() {
        val type = intent.extras?.getString("type")
        val userId = intent.extras?.getString("idUrl")
        when(type){
            MyConfigNotifi.TYPE_CHAT ->{
                navController.navigate(R.id.roomChatFragment)
                chatViewmodel.handle(ChatViewAction.findRoomSearch(userId))
            }
        }
    }

    private fun lisstenClickUI() {

    }


    private fun handleViewMolde() {

        chatViewmodel.observeViewEvents {
            when(it) {
                is ChatViewEvent.initObserverPeerConnection ->{
                }

                else -> {}
            }
        }

        chatViewmodel.subscribe(this){
            Log.e("ChatActivity", "handleViewMolde: chatViewmodel.state: curentUser -> ${it.curentUser}", )
            Log.e("ChatActivity", "handleViewMolde: chatViewmodel.state: curentRoom -> ${it.curentRoom}", )
            Log.e("ChatActivity", "handleViewMolde: chatViewmodel.state: curentCallWithUser -> ${it.curentCallWithUser}", )
            Log.e("ChatActivity", "handleViewMolde: chatViewmodel.state: requireCall -> ${it.requireCall}", )
            when(it.curentUser){
                is Success -> {
                }
                else -> {
                }
            }

            if(it.requireCall?.type == RequireCallType.OFFER_RECEIVED){
                if (navController.currentDestination?.id != R.id.callChatFragment){
                    chatViewmodel.setCallVideoWithUser(it.requireCall.myUser)
                    navController.navigate(R.id.callChatFragment)
                }
            }

            if (it.requireCallIceCandidate != null){
                var receivingIceCandidate = Gson().fromJson(it.requireCallIceCandidate?.data.toString(), IceCandidateModel::class.java)

                var iceCandidate = IceCandidate(
                    receivingIceCandidate?.sdpMid ?: "",
                    Math.toIntExact(receivingIceCandidate?.sdpMLineIndex?.toLong() ?: 0),
                    receivingIceCandidate?.sdpCandidate ?: ""
                )

                chatViewmodel.addIceCandidate(iceCandidate)
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {
        chatViewmodel.handle(ChatViewAction.returnDisconnectSocket)
        super.onDestroy()
    }

    override fun create(initialState: ChatViewState): ChatViewmodel {
        return chatViewmodelFactory.create(initialState)
    }

}
