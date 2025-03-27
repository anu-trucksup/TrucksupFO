package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.activities.vehicleVerify.VehicleDetailsActivity
import com.trucksup.field_officer.data.model.vehicle.GetVerifiedVehiclesResponse
import com.trucksup.field_officer.databinding.VerificationVehicleHistoryItemBinding

class VerifiedVehicleHistory(
    var context: Context,
    var list: ArrayList<GetVerifiedVehiclesResponse.VehicleDetail>
) : RecyclerView.Adapter<VerifiedVehicleHistory.ViewHolder>() {

    class ViewHolder(var binding: VerificationVehicleHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var view = VerificationVehicleHistoryItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.btnShare.visibility = View.GONE
//        if (list[position].status.lowercase()=="verified")
//        {
//            holder.binding.verifyImg.visibility=View.VISIBLE
//        }
//        else
//        {
//            holder.binding.verifyImg.visibility=View.GONE
//        }
        holder.binding.tvVehicleNo.text = list[position].vehicleNumber
        holder.binding.tvDateTime.text = list[position].dateTime
        holder.binding.tvOwnerName.text = list[position].dataOwnerName
        holder.binding.tvChassisNo.text = list[position].dataChasiNo

        holder.itemView.setOnClickListener {
            val intent = Intent(context, VehicleDetailsActivity::class.java)
            intent.putExtra("VEHICLE_NO", list[position].vehicleNumber)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}