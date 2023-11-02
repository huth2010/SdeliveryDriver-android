package com.fpoly.sdeliverydriver.ui.main.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentCurrentOrdersBinding

class CurrentOrdersFragment : PolyBaseFragment<FragmentCurrentOrdersBinding>(){
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCurrentOrdersBinding {
        return FragmentCurrentOrdersBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
    }



    override fun invalidate(){
//        withState(productViewModel){
//            when(it.asyncOrderings){
//                is Success -> {
//                    val adapter = OrderAdapter()
//                    views.rcyOrder.adapter = adapter
//                    adapter.setData(it.asyncOrderings.invoke())
//                }
//
//                else -> {}
//            }
//        }
    }

}