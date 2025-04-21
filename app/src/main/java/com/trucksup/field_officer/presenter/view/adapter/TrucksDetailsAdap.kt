package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.activities.preferre.modle.TrucksDetail
import com.trucksup.field_officer.databinding.AddTruckListItemsBinding
import com.trucksup.field_officer.databinding.PreferredLaneItemBinding

class TrucksDetailsAdap(
    var context: Context,
    private var trucksDetail: ArrayList<TrucksDetail>,
    var controllerListener: ControllerListener
) : RecyclerView.Adapter<TrucksDetailsAdap.ViewHolder>() {

    inner class ViewHolder(var binding: AddTruckListItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = AddTruckListItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val truckDetails = trucksDetail[position].bodytype + "/" + trucksDetail[position].tyre +
                "/" + trucksDetail[position].capacity + "/" + trucksDetail[position].vehicleSize
        viewHolder.binding.truckNo.text = trucksDetail[position].vehicleNo
        viewHolder.binding.tvTruckdetails.text = truckDetails
    }

    override fun getItemCount(): Int {
        return trucksDetail.size
    }

    interface ControllerListener {
        fun onDeleteTruck(position: Int)
    }

}