package com.fpoly.sdeliverydriver.ui.main.order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.data.model.DeliveryOrder
import com.fpoly.sdeliverydriver.databinding.ItemDeliveryOrderBinding
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.SUCCESS_STATUS

class DeliveryOrderAdapter(private val onClickItem : (String) -> Unit) : RecyclerView.Adapter<DeliveryOrderAdapter.ViewHolder>() {
    private var deliveryOrders: List<DeliveryOrder> = listOf()

    fun setData(list: List<DeliveryOrder>?) {
        if (list!=null) {
            deliveryOrders = list
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(private val binding: ItemDeliveryOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(deliveryOrder: DeliveryOrder) {
            val comment= if (deliveryOrder.comment==null) {
                "Không có nhận xét"
            }else{
                deliveryOrder.comment
            }
            binding.apply {
                root.setOnClickListener {
                    onClickItem(deliveryOrder.orderCode)
                }
                orderCodeTextView.text = "Mã đơn: ${deliveryOrder.orderCode}"
                statusTextView.text = "Trạng thái: ${deliveryOrder.status.status_name}"
                commentTextView.text = "Nhận xét: ${comment}"
                if (deliveryOrder.status._id==SUCCESS_STATUS){
                    cancelReasonTextView.visibility=View.GONE
                }else{
                    cancelReasonTextView.visibility=View.VISIBLE
                    cancelReasonTextView.text = "Lý do Hủy: ${deliveryOrder?.cancelReason?:"Không có"}"
                }

                Glide.with(root.context)
                    .load(deliveryOrder.image.url)
                    .placeholder(R.drawable.loading_img)
                    .into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDeliveryOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(deliveryOrders[position])
    }

    override fun getItemCount(): Int {
        return deliveryOrders.size
    }
}
