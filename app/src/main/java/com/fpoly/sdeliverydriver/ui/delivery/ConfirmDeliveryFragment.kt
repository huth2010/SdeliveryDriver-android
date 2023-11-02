package com.fpoly.sdeliverydriver.ui.delivery

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentConfirmDeliveryBinding

class ConfirmDeliveryFragment : PolyBaseFragment<FragmentConfirmDeliveryBinding>(){
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentConfirmDeliveryBinding {
        return FragmentConfirmDeliveryBinding.inflate(inflater,container,false)
    }

    override fun invalidate() {

    }

}