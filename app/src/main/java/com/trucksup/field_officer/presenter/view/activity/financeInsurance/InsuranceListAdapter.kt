package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.data.model.VehicleDetail
import com.trucksup.field_officer.databinding.InsListItemBinding

class InsuranceListAdapter(
    var context: Context,
    var list: ArrayList<VehicleDetail>,
    var cantroler: InsuranceController
) : RecyclerView.Adapter<InsuranceListAdapter.ViewHolder>() {

    class ViewHolder(var binding: InsListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = InsListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvVehicleNo.text = list[position].vehicleNumber
        holder.binding.tvDate.text = list[position].insuranceValidity
        //delete list item
        holder.binding.imgDelete.setOnClickListener {
            cantroler.deleteTruck(position)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addListItem(vehicleNo: String, date: String) {
        list.add(0, VehicleDetail(date, vehicleNo,"","","","","",""))

    }
}