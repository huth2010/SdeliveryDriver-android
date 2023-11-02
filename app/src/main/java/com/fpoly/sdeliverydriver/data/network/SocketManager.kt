package com.fpoly.sdeliverydriver.data.network

import android.util.Log
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SocketManager @Inject constructor() {

    companion object {
        const val CLIENT_LISTEN_ROOM: String = "client-listen-room-"
        const val CLIENT_LISTEN_MESSAGE: String = "client-listen-message-"

        const val SERVER_LISTEN_CALL: String = "server-listen-call"
        const val CLIENT_LISTEN_CALL: String = "client-listen-call-"
    }

    private lateinit var mSocket: Socket
    val socket get () = mSocket

    init {
        this.mSocket = IO.socket(RemoteDataSource.BASE_URL.replace("3000", "3001"))
    }

    public fun connect(){
        mSocket.connect()
        Log.e("SocketManager", "connect: connect  ${mSocket.hashCode()}", )
    }

    public fun disconnect(){
        mSocket.disconnect()
        Log.e("SocketManager", "connect: disconnect  ${mSocket.hashCode()}", )
    }


    public fun sendEmitToSocket(toEvent: String, data: Any){
        if (mSocket.connected() == true){
            mSocket.emit(toEvent, Gson().toJson(data))
        }else{
            Log.e("SocketManager", "socket: isDisconnect", )
        }
    }
//    private val socketListeners: MutableMap<String, MutableList<() -> Unit>> = HashMap()

    public fun <T> onReceiveEmitSocket(event: String, type: Class<T>, callBack: (data: T?) -> Unit) {
        Log.e("SocketManager", "onReceiveEmitSocket: connected: ${mSocket.connected()} event $event")
        if (mSocket.connected() == true) {
//            if (!socketListeners.containsKey(event)) {
//                socketListeners[event] = mutableListOf()

                mSocket.on(event) {
                    if (!it[0].toString().isNullOrEmpty()) {
//                        val listeners = socketListeners[event]
                        CoroutineScope(Dispatchers.Main).launch {
//                            listeners?.forEach { listener -> listener() }

                            callBack(Gson().fromJson(it[0].toString(), type))
                        }
                    } else {
                        Log.e("SocketManager", "receive: event $event -> null")
                    }
                }
//            }
//            socketListeners[event]?.add { callBack(null) }
        }
    }







public fun offReceiEmitSocket(event: String) {
        if (mSocket.connected() == true){
            mSocket.off(event)
        }
    }
}