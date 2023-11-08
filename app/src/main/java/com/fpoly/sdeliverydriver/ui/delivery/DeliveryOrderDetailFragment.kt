package com.fpoly.sdeliverydriver.ui.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.data.model.ProductCart
import com.fpoly.sdeliverydriver.databinding.FragmentDeliveryOrderDetailBinding
import com.fpoly.sdeliverydriver.ui.delivery.adapter.ProductCartAdapter
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewModel

class DeliveryOrderDetailFragment : PolyBaseFragment<FragmentDeliveryOrderDetailBinding>(){
    private val homeViewModel: HomeViewModel by activityViewModel()
    private var currentOrder: OrderResponse? = null

    private var productCartAdapter: ProductCartAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        listenEvent()
    }

    private fun setupAppBar() {
        views.appBar.apply {
            btnBackToolbar.visibility = View.VISIBLE
            tvTitleToolbar.text = getString(R.string.order)
        }
    }

    private fun listenEvent() {
        views.appBar.btnBackToolbar.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun setupUI(currentOrder: OrderResponse) {
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
        }
        setupListProductCart(currentOrder.products)
    }

    private fun setupListProductCart(products: List<ProductCart>) {
        productCartAdapter = ProductCartAdapter()
        views.rcCart.adapter=productCartAdapter
        productCartAdapter?.setData(products)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDeliveryOrderDetailBinding {
        return FragmentDeliveryOrderDetailBinding.inflate(inflater,container,false)
    }

    override fun invalidate():Unit= withState(homeViewModel){
        when(it.asyncGetCurrentOrder){
            is Success -> {
                currentOrder= it.asyncGetCurrentOrder.invoke()
                currentOrder?.let { currentOrder -> setupUI(currentOrder) }
            }

            else -> {}
        }

    }

}