package com.fpoly.sdeliverydriver.ui.delivery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.sdeliverydriver.R

class CancelReasonAdapter(
    private val cancelReasons: List<String>,
    private val onItemClickListener: (String) -> Unit
) : RecyclerView.Adapter<CancelReasonAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textCancelReason: TextView = view.findViewById(R.id.textCancelReason)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cancel_reason, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cancelReason = cancelReasons[position]
        holder.textCancelReason.text = cancelReason
        holder.itemView.setOnClickListener { onItemClickListener(cancelReason) }
    }

    override fun getItemCount(): Int {
        return cancelReasons.size
    }
}
