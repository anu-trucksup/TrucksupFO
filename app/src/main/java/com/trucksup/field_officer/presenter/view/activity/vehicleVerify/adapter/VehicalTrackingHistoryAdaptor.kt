package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

import com.logistics.trucksup.modle.TrackingHistory
import com.logistics.trucksup.modle.VehicleTrack
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.VehicalTrip


class VehicalTrackingHistoryAdaptor(val context: Context, var data: ArrayList<TrackingHistory>) :
    RecyclerView.Adapter<VehicalTrackingHistoryAdaptor.ViewHolder>() {
    var co: Int = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val truckNumber: TextView = itemView.findViewById<TextView>(R.id.truckNumber)
        val endDate: TextView = itemView.findViewById<TextView>(R.id.endDate)
        val startDate: TextView = itemView.findViewById<TextView>(R.id.startDate)
        val to: TextView = itemView.findViewById(R.id.to)
        val from: TextView = itemView.findViewById(R.id.from)
        val fromToLayout: LinearLayout = itemView.findViewById(R.id.fromToLayout)


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehicalTrackingHistoryAdaptor.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.vehical_tracking_history, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(
        viewHolder: VehicalTrackingHistoryAdaptor.ViewHolder,
        position: Int
    ) {

        if (data[position].fromLocation != null && !TextUtils.isEmpty(data[position].fromLocation)) {
            viewHolder.fromToLayout?.visibility = View.VISIBLE
            viewHolder.from.setText(data[position].fromLocation)
        } else {
            viewHolder.fromToLayout?.visibility = View.GONE
            viewHolder.from.setText("")
        }

        if (data[position].toLocation != null) {
            viewHolder.to.setText(data[position].toLocation)
        } else {

            viewHolder.to.setText("")
        }

        if (data[position].tripStartDate != null) {
            viewHolder.startDate.setText(data[position].tripStartDate)
        } else {
            viewHolder.startDate.setText("")
        }

        if (data[position].tripEndDate != null) {
            viewHolder.endDate.setText(data[position].tripEndDate)
        } else {
            viewHolder.endDate.setText("")
        }


        if (data[position].truckNumber != null) {
            viewHolder.truckNumber.setText(data[position].truckNumber)
        } else {
            viewHolder.truckNumber.setText("")
        }



        viewHolder.itemView.setOnClickListener {


            val gson = Gson()
            var intent: Intent = Intent(context, VehicalTrip::class.java)
            intent.putExtra("type", "h")
            intent.putExtra("data", gson.toJson(data[position]))

            context.startActivity(intent)
        }


    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return data.size
    }
}