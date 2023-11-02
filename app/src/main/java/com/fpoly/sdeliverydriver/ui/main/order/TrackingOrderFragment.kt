package com.fpoly.sdeliverydriver.ui.main.order

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentTrackingOrderBinding

class TrackingOrderFragment : PolyBaseFragment<FragmentTrackingOrderBinding>() {


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTrackingOrderBinding {
        return FragmentTrackingOrderBinding.inflate(inflater,container,false)
    }

    override fun invalidate() {
    }

}