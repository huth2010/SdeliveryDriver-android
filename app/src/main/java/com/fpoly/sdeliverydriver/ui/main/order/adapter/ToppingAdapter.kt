package com.fpoly.sdeliverydriver.ui.main.order.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.sdeliverydriver.data.model.Topping
import com.fpoly.sdeliverydriver.databinding.ItemToppingViewBinding
import com.fpoly.sdeliverydriver.ultis.formatCash

@SuppressLint("NotifyDataSetChanged")
class ToppingAdapter(): RecyclerView.Adapter<ToppingAdapter.ViewHolderView>() {
    var toppings: ArrayList<Topping>? = null
    fun setData(data: List<Topping>?) {
        if (data == null) return
        toppings = data as ArrayList<Topping>
        notifyDataSetChanged()
    }
    fun getToppingsSelect(): List<Topping>? = toppings?.filter { it.quantity > 0 }

    fun getTotalPriceTopping(): Double {
        var totalPrice: Double = 0.0
        toppings?.forEach{
            totalPrice += (it.price * it.quantity)
        }
        return totalPrice
    }
    override fun getItemCount(): Int {
        if (toppings != null) return toppings!!.size
        return 0
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToppingAdapter.ViewHolderView {
        return ViewHolderView(ItemToppingViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ToppingAdapter.ViewHolderView, position: Int) {
        holder.onBind(toppings!![position])
    }

    inner class ViewHolderView(val binding: ItemToppingViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(topping: Topping){
            binding.tvName.text = topping.name
            binding.tvPrice.text = topping.price.formatCash()
            binding.tvQuantity.text = "x${topping.quantity}"
        }
    }
}