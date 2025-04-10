package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.AddTruckListItemsBinding
import com.trucksup.field_officer.databinding.AddTsTruckDetailsItemsBinding
import com.trucksup.field_officer.databinding.PreferredLaneItemBinding
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.model.VehicleDetail

class TSTrucksDetailsAdapter(
    var context: Context,
    var trucksDetail: ArrayList<VehicleDetail>,
    var controllerListener: ControllerListener
) : RecyclerView.Adapter<TSTrucksDetailsAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: AddTsTruckDetailsItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = AddTsTruckDetailsItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val truckdetails = trucksDetail[position].bodytype + "/" + trucksDetail[position].tyre +
            "/" + trucksDetail[position].capacity + "/" + trucksDetail[position].vehicleSize
        viewHolder.binding.truckNo.text = trucksDetail[position].vehicleNo
        viewHolder.binding.tvTruckdetails.text = "" + truckdetails
    }

    override fun getItemCount(): Int {
        return trucksDetail.size
    }

    interface ControllerListener {
        fun onDeleteTruck(position: Int)
    }

}