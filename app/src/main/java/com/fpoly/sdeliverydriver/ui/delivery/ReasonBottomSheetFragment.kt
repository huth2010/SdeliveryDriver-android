package com.fpoly.sdeliverydriver.ui.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseBottomSheet
import com.fpoly.sdeliverydriver.databinding.FragmentReasonBottomSheetBinding
import com.fpoly.sdeliverydriver.ui.delivery.adapter.CancelReasonAdapter

class ReasonBottomSheetFragment : PolyBaseBottomSheet<FragmentReasonBottomSheetBinding>() {
    private val deliveryViewModel: DeliveryViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        val adapter = CancelReasonAdapter(createCancelReasonsList()){
            deliveryViewModel.handle(DeliveryViewAction.SetCurrentCancelReason(it))
            dismiss()
        }
        views.recyclerViewCancelReasons.adapter=adapter
    }

    private fun createCancelReasonsList(): List<String> {
        return listOf(
            "Vấn đề về kiện hàng",
            "Vấn đề về người nhận",
            "Vấn đề khác"
        )
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReasonBottomSheetBinding {
        return FragmentReasonBottomSheetBinding.inflate(inflater,container,false)
    }

    override fun invalidate() {
    }

}