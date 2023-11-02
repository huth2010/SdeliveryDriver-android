package com.fpoly.sdeliverydriver.ui.delivery

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentProblemDetailBinding

class ProblemDetailFragment : PolyBaseFragment<FragmentProblemDetailBinding>(){
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProblemDetailBinding {
        return FragmentProblemDetailBinding.inflate(inflater,container,false)
    }

    override fun invalidate() {

    }

}