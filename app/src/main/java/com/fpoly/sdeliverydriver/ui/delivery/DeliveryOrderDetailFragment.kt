package com.fpoly.sdeliverydriver.ui.delivery

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentDeliveryOrderDetailBinding

class DeliveryOrderDetailFragment : PolyBaseFragment<FragmentDeliveryOrderDetailBinding>(){
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDeliveryOrderDetailBinding {
        return FragmentDeliveryOrderDetailBinding.inflate(inflater,container,false)
    }

    override fun invalidate() {

    }

}