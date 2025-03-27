package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.PurchasedPlanItemBinding

class PurchasedPlanAdap(var context: Context, var list: ArrayList<String>) :
    RecyclerView.Adapter<PurchasedPlanAdap.ViewHolder>() {

    class ViewHolder(var binding: PurchasedPlanItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchasedPlanAdap.ViewHolder {
        var binding = PurchasedPlanItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PurchasedPlanAdap.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }
}