package com.fpoly.sdeliverydriver.ui.main.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentHistoryOrderBinding

class HistoryOrderFragment :  PolyBaseFragment<FragmentHistoryOrderBinding>(){
   // private val productViewModel: ProductViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
    }

    override fun invalidate(){
//        withState(productViewModel){
//        when(it.asyncOrders){
//            is Success -> {
//                val adapter = OrderAdapter()
//                views.rcyOrder.adapter = adapter
//                adapter.setData(it.asyncOrders.invoke())
//            }
//
//            else -> {}
//        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHistoryOrderBinding {
        return FragmentHistoryOrderBinding.inflate(inflater,container,false)
    }

}