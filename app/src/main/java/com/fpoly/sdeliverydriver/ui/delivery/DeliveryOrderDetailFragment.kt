package com.fpoly.sdeliverydriver.ui.delivery

import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.data.model.ProductCart
import com.fpoly.sdeliverydriver.data.model.ProductOrder
import com.fpoly.sdeliverydriver.databinding.FragmentDeliveryOrderDetailBinding
import com.fpoly.sdeliverydriver.ui.delivery.adapter.ProductCartAdapter
import com.fpoly.sdeliverydriver.ui.delivery.adapter.ProductOrderAdapter
import com.fpoly.sdeliverydriver.ultis.formatCash

class DeliveryOrderDetailFragment : PolyBaseFragment<FragmentDeliveryOrderDetailBinding>(){
    private val deliveryViewModel: DeliveryViewModel by activityViewModel()
    private var currentOrder: OrderResponse? = null

    private lateinit var productOrderAdapter: ProductOrderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        listenEvent()
    }

    private fun initUI() {
        views.appBar.apply {
            btnBackToolbar.visibility = View.VISIBLE
            tvTitleToolbar.text = getString(R.string.order)
        }

        productOrderAdapter = ProductOrderAdapter{

        }
        views.rcvProduct.adapter = productOrderAdapter
    }

    private fun listenEvent() {
        views.appBar.btnBackToolbar.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        views.btnSuccess.setOnClickListener {
            findNavController().navigate(R.id.action_deliveryOrderDetailFragment_to_confirmDeliveryFragment)
        }
        views.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_deliveryOrderDetailFragment_to_problemFragment)
        }
    }

    private fun setupUI(currentOrder: OrderResponse) {
        productOrderAdapter.setData(currentOrder)

        views.apply {
            Glide.with(requireContext())
                .load(currentOrder.userId.avatar?.url)
                .placeholder(R.drawable.baseline_person_outline_24)
                .error(R.drawable.baseline_person_outline_24)
                .into(imgAvatar)
            userName.text= "${currentOrder.userId.first_name}${currentOrder.userId.last_name}"
            recipientName.text = currentOrder.address.recipientName
            orderTimeValue.text = currentOrder.createdAt
            pickupTimeValue.text =currentOrder.updatedAt
            orderCodeValue.text = currentOrder._id
            location.text = currentOrder.address.addressLine
            phoneNumber.text = currentOrder.address.phoneNumber
            phone.text = currentOrder.address.phoneNumber

            Linkify.addLinks(phone, Linkify.PHONE_NUMBERS)
            phone.setLinkTextColor(root.context.resources.getColor(R.color.black))

            tvTotal.text = currentOrder.total.formatCash()
            tvDiscount.text = currentOrder.discount.formatCash()
            tvDeliverfee.text = currentOrder.deliveryFee.formatCash()
            tvPrice.text = currentOrder.totalAll.formatCash()
            tvTypePaymentName.text = currentOrder.statusPayment.status_name
            tvIsPayment.text = if (currentOrder.isPayment) "Đã thanh toán" else "Chưa thanh toán"

        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDeliveryOrderDetailBinding {
        return FragmentDeliveryOrderDetailBinding.inflate(inflater,container,false)
    }

    override fun invalidate():Unit= withState(deliveryViewModel){
        when(it.asyncGetCurrentOrder){
            is Success -> {
                currentOrder= it.asyncGetCurrentOrder.invoke()
                currentOrder?.let { currentOrder -> setupUI(currentOrder) }
            }

            else -> {}
        }
        when (it.asyncCreateDelivery) {
            is Success -> {
                findNavController().popBackStack()
            }

            else -> {}
        }

    }

}