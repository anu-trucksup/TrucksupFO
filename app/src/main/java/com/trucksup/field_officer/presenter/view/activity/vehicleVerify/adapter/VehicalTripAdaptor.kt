package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.modle.Milestone
import com.trucksup.field_officer.R

class VehicalTripAdaptor (val context: Context,var data: ArrayList<Milestone>) : RecyclerView.Adapter<VehicalTripAdaptor.ViewHolder>()
{
    var co:Int=0
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mapPin: ImageButton =  itemView.findViewById(R.id.pin)
        val date: TextView =itemView.findViewById(R.id.startDate)
        val city: TextView =itemView.findViewById(R.id.city)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicalTripAdaptor.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.vehical_trip_item, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(viewHolder: VehicalTripAdaptor.ViewHolder, position: Int) {


          if (position==0)
          {
              viewHolder.mapPin.setImageResource(R.drawable.map_pin_green_17)
          }
        else{
             if (position==itemCount-1)
             {
                 viewHolder.mapPin.setImageResource(R.drawable.map_pin_red_17)
             }
              else{
                 viewHolder.mapPin.setImageResource(R.drawable.map_pin_brown_17)
             }
          }

        if (data[position].address!=null)
        {
            viewHolder.city.setText(data[position].address)
        }

        if (data[position].time!=null)
        {
            viewHolder.date.setText(data[position].time)
        }


    }
    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return data.size
    }
}