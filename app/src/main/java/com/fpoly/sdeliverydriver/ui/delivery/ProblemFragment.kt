package com.fpoly.sdeliverydriver.ui.delivery

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentProblemBinding

class ProblemFragment : PolyBaseFragment<FragmentProblemBinding>(){
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProblemBinding {
        return FragmentProblemBinding.inflate(inflater,container,false)
    }

    override fun invalidate() {

    }

}