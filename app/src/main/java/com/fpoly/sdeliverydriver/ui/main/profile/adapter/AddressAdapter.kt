package com.fpoly.sdeliverydriver.ui.main.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.sdeliverydriver.data.model.Address
import com.fpoly.sdeliverydriver.databinding.ItemAddressBinding

class AddressAdapter(val onClick: (Address) -> Unit) :
RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    private var addresss: List<Address> = listOf()

    fun updateSelectedAddress(selectedAddress: Address) {
        addresss.forEach { address ->
            address.isSelected = address == selectedAddress
        }
        notifyDataSetChanged()
    }

    fun setData(list: List<Address>?) {
        if (list!= null){
            addresss = list
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder =
        AddressViewHolder(
            ItemAddressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addresss[position]
        holder.bind(address)
    }

    override fun getItemCount() = addresss.size

    inner class AddressViewHolder(private val itemAddressBinding: ItemAddressBinding) :
        RecyclerView.ViewHolder(itemAddressBinding.root) {
        fun bind(address: Address) {
            itemAddressBinding.recipientName.text = address.recipientName
            itemAddressBinding.phone.text = address.phoneNumber
            itemAddressBinding.address.text = address.addressLine
            itemAddressBinding.radioButtonAddress.isChecked = address.isSelected

            itemAddressBinding.radioButtonAddress.setOnClickListener {
                onClick(address)
            }

        }
    }
}