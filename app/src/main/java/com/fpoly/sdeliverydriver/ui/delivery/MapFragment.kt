package com.fpoly.sdeliverydriver.ui.delivery

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentMapBinding

class MapFragment : PolyBaseFragment<FragmentMapBinding>(){
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMapBinding {
        return FragmentMapBinding.inflate(inflater,container,false)
    }

    override fun invalidate() {

    }

}