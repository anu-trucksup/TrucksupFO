package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
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

        if (!list[position].PolicyDoc.isNullOrEmpty() || !list[position].RCFrontImgKey.isNullOrEmpty() || !list[position].RCBackImgKey.isNullOrEmpty()) {
            holder.binding.tvView.visibility = View.VISIBLE
        } else {
            holder.binding.tvView.visibility = View.INVISIBLE
        }

        //delete list item
        holder.binding.tvView.setOnClickListener {
//            cantroler.deleteTruck(position)
//            controllerListener2?.onDeleteTruck(position)
            controllerListener2?.onShowTruckDetails(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    var controllerListener2: ControllerListener2? = null

    interface ControllerListener2 {
        fun onDeleteTruck(position: Int)
        fun onShowTruckDetails(data: VehicleDetail)
    }

    fun setOnControllerListener2(controllerListener2: ControllerListener2) {
        this.controllerListener2 = controllerListener2
    }
}