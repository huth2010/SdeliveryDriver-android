package com.fpoly.sdeliverydriver.ui.delivery.adapter


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.data.model.ProductOrder
import com.fpoly.sdeliverydriver.data.model.Topping
import com.fpoly.sdeliverydriver.databinding.ItemProductOrderBinding
import com.fpoly.sdeliverydriver.ui.main.order.adapter.ToppingAdapter
import com.fpoly.sdeliverydriver.ultis.formatCash


class ProductOrderAdapter(private val onPress: (productOrder: ProductOrder) -> Unit): RecyclerView.Adapter<ProductOrderAdapter.ViewHolder>() {
    var orderResporn: OrderResponse? = null

    fun setData(data: OrderResponse?){
        if (data == null) return
        this.orderResporn = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductOrderBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(productOrder: ProductOrder, position: Int){
            binding.apply{
                Glide.with(this.root.context).load(productOrder.image).placeholder(R.mipmap.ic_launcher).into(imgImg)
                tvName.text = productOrder.product_name
                tvType.text = "Size ${productOrder.sizeName}"
                tvQuantity.text = "x${productOrder.purchase_quantity}"
                tvPrice.text = productOrder.product_price.formatCash()

                tvAllPrice.text = productOrder.total.formatCash()

                if (productOrder.toppings.isNotEmpty()){
                    val toppingAdapter = ToppingAdapter()
                    toppingAdapter.setData(productOrder.toppings.map { Topping.toTopping(it) })
                    rcvToping.adapter = toppingAdapter
                }else{
                    rcvToping.adapter = null
                }

                root.setOnClickListener {
                    onPress(productOrder)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProductOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        if (orderResporn?.products != null) return orderResporn?.products!!.size
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (orderResporn?.products != null){
            holder.onBind(orderResporn?.products!![position], position)
        }
    }
}