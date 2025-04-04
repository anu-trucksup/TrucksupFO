package com.trucksup.field_officer.presenter.view.activity.subscription

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.modle.Broker
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import java.text.StringCharacterIterator


class SubscriptionAdapterTest(
    val context: Context,
    var data: ArrayList<Broker>,
    var cantroler: PaySubscribtion
) : RecyclerView.Adapter<SubscriptionAdapterTest.ViewHolder>() {
    var co: Int = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var main_cat: LinearLayout = itemView.findViewById(R.id.main_cat)
        val amount: TextView = itemView.findViewById<TextView>(R.id.amount)
        var recomanded: RelativeLayout = itemView.findViewById(R.id.recomanded)
        var planType: TextView = itemView.findViewById(R.id.planType)
        var amountDay: TextView = itemView.findViewById(R.id.amountDay)
        var days: TextView = itemView.findViewById(R.id.days)
        var loads: TextView = itemView.findViewById(R.id.loads)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscriptionAdapterTest.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.new_sub_items, parent, false)


        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(viewHolder: SubscriptionAdapterTest.ViewHolder, position: Int) {
    }

    // Returns
    // the total count of items in the list
    override fun getItemCount(): Int {
        return 4
    }


}