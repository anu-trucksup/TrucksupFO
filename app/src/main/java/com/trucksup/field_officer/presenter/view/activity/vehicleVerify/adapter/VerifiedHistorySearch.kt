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

class VerifiedHistorySearch(var context: Context, var list:ArrayList<GetVerifiedVehiclesResponse.VehicleDetail>): RecyclerView.Adapter<VerifiedHistorySearch.ViewHolder>() {

    private var controlListener: ControlListener? = null

    class ViewHolder(var binding: VerificationVehicleHistoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var view=
            VerificationVehicleHistoryItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.btnShare.visibility=View.VISIBLE
        if (list[position].status.lowercase()=="verified")
        {
            holder.binding.verifyImg.visibility=View.VISIBLE
        }
        else
        {
            holder.binding.verifyImg.visibility=View.GONE
        }
        holder.binding.tvVehicleNo.text=list[position].vehicleNumber
        holder.binding.tvDateTime.text=list[position].dateTime
        holder.binding.tvOwnerName.text=list[position].dataOwnerName
        holder.binding.tvChassisNo.text=list[position].dataChasiNo

        holder.binding.btnShare.setOnClickListener {
//            controlListener?.onShareClick(holder.binding.card)
            controlListener?.onShareClick(holder.binding.card,list[position].vehicleNumber)

//            var shareData=context.resources.getString(R.string.vehical_no)+list[position].vehicleNumber+"\n"+
//                    context.resources.getString(R.string.date_time)+list[position].dateTime+"\n"+
//                    context.resources.getString(R.string.owner_name)+list[position].dataOwnerName+"\n"+
//                    context.resources.getString(R.string.chassis_number)+list[position].dataChasiNo
//            val sendIntent: Intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_TEXT, shareData)
//                type = "text/plain"
//            }
//            val shareIntent = Intent.createChooser(sendIntent, null)
//            context.startActivity(shareIntent)
        }

        holder.itemView.setOnClickListener {


            val intent=Intent(context, VehicleDetailsActivity::class.java)
            intent.putExtra("VEHICLE_NO",list[position].vehicleNumber)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    // method for filtering our recyclerview items
    fun filterList(filteredList:ArrayList<GetVerifiedVehiclesResponse.VehicleDetail>) {
        // below line is to add our filtered
        // list in our course array list.
        list = filteredList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    fun setControlListener(controlListener: ControlListener) {
        this.controlListener = controlListener
    }

    interface ControlListener {
        fun onShareClick(v:View,vehicleNumber:String)
    }

}