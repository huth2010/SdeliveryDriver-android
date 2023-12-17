package com.fpoly.sdeliverydriver.ui.main.order

import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.fpoly.sdeliverydriver.databinding.FragmentOrderDetailBinding
import com.fpoly.sdeliverydriver.ui.chat.ChatActivity
import com.fpoly.sdeliverydriver.ui.delivery.adapter.ProductCartAdapter
import com.fpoly.sdeliverydriver.ui.delivery.adapter.ProductOrderAdapter
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewModel
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.SUCCESS_STATUS
import com.fpoly.sdeliverydriver.ultis.MyConfigNotifi
import com.fpoly.sdeliverydriver.ultis.formatCash

class DeliveryDetailFragment : PolyBaseFragment<FragmentOrderDetailBinding>() {
    private val homeViewModel: HomeViewModel by activityViewModel()
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
        views.btnChat.setOnClickListener {
            if (currentOrder?.userId?._id != null){
                val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putString("type", MyConfigNotifi.TYPE_CHAT)
                        putString("idUrl", currentOrder?.userId?._id ?: "")
                    }
                    )
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }else{
                Toast.makeText(requireContext(), "Không tìm thấy người giao hàng", Toast.LENGTH_SHORT).show()
            }
        }
        views.btnCall.setOnClickListener {
            if (currentOrder?.userId?._id != null){
                val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putString("type", MyConfigNotifi.TYPE_CALL_OFFER)
                        putString("idUrl", currentOrder?.userId?._id ?: "")
                    }
                    )
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }else{
                Toast.makeText(requireContext(), "Không tìm thấy người giao hàng", Toast.LENGTH_SHORT).show()
            }
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
    ): FragmentOrderDetailBinding {
        return FragmentOrderDetailBinding.inflate(inflater,container,false)
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