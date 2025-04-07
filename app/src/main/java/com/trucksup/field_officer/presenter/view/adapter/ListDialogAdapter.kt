package com.trucksup.field_officer.presenter.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.data.model.FromToModel
import com.trucksup.field_officer.databinding.ItemActiveBaBinding
import com.trucksup.field_officer.databinding.ItemSubscriptionPurchaseHistoryBinding
import com.trucksup.field_officer.databinding.ListDialogBinding
import com.trucksup.field_officer.databinding.PreferredLaneDialogBinding

class ListDialogAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<ListDialogAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemSubscriptionPurchaseHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemSubscriptionPurchaseHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.planPurchessPrice.setText("₹ 229")
    }

    override fun getItemCount(): Int {
        return list.size
    }

}