package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.logistics.trucksup.modle.VehicleTrack
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.VehicalTrip
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.VehicalListClickCantroler

class VehicalTrackingDataAdaptor(val context: Context, var data: ArrayList<VehicleTrack>) :
    RecyclerView.Adapter<VehicalTrackingDataAdaptor.ViewHolder>() {
    var clickCantroler: VehicalListClickCantroler = context as VehicalListClickCantroler
    var co: Int = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deActiveLayout: MaterialCardView = itemView.findViewById(R.id.deActiveLayout)
        var activeLayout: MaterialCardView = itemView.findViewById(R.id.activeLayout)
        var sendConsent = itemView.findViewById<TextView>(R.id.sendConsent)
        val btTrip: TextView = itemView.findViewById<TextView>(R.id.btTrip)
        val btTrip_one: TextView = itemView.findViewById<TextView>(R.id.btTrip_one)
        val vehicalOne: TextView = itemView.findViewById<TextView>(R.id.vehicalOne)
        val vehicalTwo: TextView = itemView.findViewById<TextView>(R.id.vehicalTwo)
        val date: TextView = itemView.findViewById(R.id.date)


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehicalTrackingDataAdaptor.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.tracking_vehical_items, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(
        viewHolder: VehicalTrackingDataAdaptor.ViewHolder,
        position: Int
    ) {


        if (data.get(position).consentStatus == "ALLOWED") {
            viewHolder.activeLayout?.visibility = View.VISIBLE
            viewHolder.deActiveLayout?.visibility = View.GONE
        } else {
            viewHolder.activeLayout?.visibility = View.GONE
            viewHolder.deActiveLayout?.visibility = View.VISIBLE
        }

        if (data[position].truckNumber != null) {
            viewHolder.vehicalOne.setText(data[position].truckNumber)
            viewHolder.vehicalTwo.setText(data[position].truckNumber)
        } else {
            viewHolder.vehicalOne.setText("")
            viewHolder.vehicalTwo.setText("")
        }

        if (data[position].tripStartDate != null && !TextUtils.isEmpty(data[position].tripStartDate)) {
            viewHolder.date.setText(data[position].tripStartDate)
        } else {
            viewHolder.date.setText("")
        }


        viewHolder.sendConsent.setOnClickListener {
            clickCantroler.reSentConsent(data[position].driverMobileNumber)
        }

        viewHolder.btTrip_one.setOnClickListener {

            val gson = Gson()
            var intent: Intent = Intent(context, VehicalTrip::class.java)
            intent.putExtra("type", "t")
            intent.putExtra("data", gson.toJson(data[position]))

            context.startActivity(intent)
        }
        viewHolder.btTrip.setOnClickListener {

            val gson = Gson()
            var intent: Intent = Intent(context, VehicalTrip::class.java)
            intent.putExtra("data", gson.toJson(data[position]))

            context.startActivity(intent)
        }

    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return data.size
    }
}