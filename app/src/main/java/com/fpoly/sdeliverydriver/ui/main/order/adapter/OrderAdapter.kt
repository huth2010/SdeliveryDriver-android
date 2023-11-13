package com.fpoly.sdeliverydriver.ui.main.order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.databinding.ItemOrderBinding

class OrderAdapter(private val onClickButton: (String) -> Unit) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private var orders: List<OrderResponse> = listOf()

    fun setData(list: List<OrderResponse>?) {
        if (list != null) {
            orders = list
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentOrder = orders[position]
        holder.bind(currentOrder)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class OrderViewHolder(private val itemOrderBinding: ItemOrderBinding) :
        RecyclerView.ViewHolder(itemOrderBinding.root) {
        fun bind(order: OrderResponse) {
            itemOrderBinding.apply {
                idOrder.text = order._id
                status.text = order.status.status_name
                nameProduct.text = order.products[0].product_name
                recipientName.text = order.address.recipientName
                phone.text = order.address.phoneNumber
                address.text = order.address.addressLine
            }

            itemOrderBinding.btnTakeOrder.setOnClickListener {
            onClickButton(order._id)
            }
        }
    }
}
