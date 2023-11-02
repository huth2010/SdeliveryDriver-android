package com.fpoly.sdeliverydriver.ui.chat.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentSearchChatBinding
import com.fpoly.sdeliverydriver.ui.chat.ChatViewAction
import com.fpoly.sdeliverydriver.ui.chat.ChatViewmodel

class SearchChatFragment : PolyBaseFragment<FragmentSearchChatBinding>(){

    lateinit var adapter: SearchChatAdapter

    private val chatViewModel: ChatViewmodel by activityViewModel()

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchChatBinding = FragmentSearchChatBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        lisstenUI()

    }

    private fun initUI() {
        views.edtTitle.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(views.edtTitle, InputMethodManager.SHOW_IMPLICIT)

        adapter = SearchChatAdapter{
            findNavController().navigate(R.id.roomChatFragment)
            chatViewModel.handle(ChatViewAction.findRoomSearch(it))
        }
        views.rcv.adapter = adapter
        views.rcv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun lisstenUI() {
        views.imgBack.setOnClickListener{
            activity?.onBackPressed()
        }

        views.imgClear.setOnClickListener{
            views.edtTitle.setText("")
        }

        views.edtTitle.doOnTextChanged { text, start, before, count ->
            views.imgClear.isVisible = text.toString().length != 0

            if (text.toString().length == 0){
                views.tvFinding.text = "Hãy tìm kiếm"
            }else{
                views.tvFinding.text = "Xem kết quả cho: $text"
            }

            views.imgLoading.isVisible = text.toString().length != 0

            if (text.toString().isNotEmpty()){
                chatViewModel.handle(ChatViewAction.searchUserByName(text.toString()))
            }
        }

    }

    override fun invalidate(): Unit = withState(chatViewModel){
        when(it.curentUsersSreach){
            is Success ->{
               adapter.setData(it.curentUsersSreach.invoke())

            views.tvExists.isVisible = it.curentUsersSreach.invoke().isEmpty()
            }
            is Fail ->{
                adapter.clearData()
                views.tvExists.isVisible = true
            }
            is Loading ->{
                adapter.clearData()
            }
            else -> {}
        }

        views.imgLoading.isVisible = it.curentUsersSreach is Loading
    }
}