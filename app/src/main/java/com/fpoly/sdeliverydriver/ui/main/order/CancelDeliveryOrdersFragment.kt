package com.fpoly.sdeliverydriver.ui.main.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentCancelDeliveryOrdersBinding
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewAction
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewModel
import com.fpoly.sdeliverydriver.ui.main.order.adapter.DeliveryOrderAdapter

class CancelDeliveryOrdersFragment :  PolyBaseFragment<FragmentCancelDeliveryOrdersBinding>(){
    private val homeViewModel: HomeViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun invalidate(){
        withState(homeViewModel){
            when(it.asyncSuccessDeliveries){
                is Success -> {
                    val adapter = DeliveryOrderAdapter{
                        homeViewModel.handle(HomeViewAction.GetCurrentOrder(it))
                        findNavController().navigate(R.id.action_DeliveryOrderFragment_to_deliveryDetailFragment)
                    }
                    views.rcyDeliveryOrder.adapter = adapter
                    adapter.setData(it.asyncSuccessDeliveries.invoke())
                }

                else -> {}
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCancelDeliveryOrdersBinding {
        return FragmentCancelDeliveryOrdersBinding.inflate(inflater,container,false)
    }

}