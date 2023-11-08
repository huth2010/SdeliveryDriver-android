package com.fpoly.sdeliverydriver.ui.delivery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.data.model.ProductCart
import com.fpoly.sdeliverydriver.databinding.ItemCartBinding


class ProductCartAdapter : RecyclerView.Adapter<ProductCartAdapter.CartViewHolder>() {
    private var productsCart: List<ProductCart> = listOf()
    fun setData(list: List<ProductCart>?) {
        if (list != null) {
            productsCart = list
            notifyDataSetChanged()
        }
    }

    inner class CartViewHolder(private val binding: ItemCartBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        val image = binding.image
        val name = binding.nameProductSheet
        val price = binding.priceProductSheet
        val size = binding.size


        fun bind(currentProduct: ProductCart) {
            Glide.with(context).load(currentProduct.image).placeholder(R.drawable.loading_img)
                .into(image)
            name.text = currentProduct.product_name
            price.text = currentProduct.product_price.toString()
            size.text = "Size " + currentProduct.sizeName

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(inflater, parent, false)
        return CartViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentProduct: ProductCart = productsCart[position]
        holder.bind(currentProduct)

    }

    override fun getItemCount(): Int {
        return productsCart.size
    }

}
