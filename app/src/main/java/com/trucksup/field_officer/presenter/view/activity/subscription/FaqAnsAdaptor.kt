package com.trucksup.field_officer.presenter.view.activity.subscription

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.modle.Ans
import com.logistics.trucksup.modle.OwnerFaq
import com.trucksup.field_officer.R

class FaqAnsAdaptor(val context: Context, var data: ArrayList<Ans>) :
    RecyclerView.Adapter<FaqAnsAdaptor.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val ans: TextView = itemView.findViewById<TextView>(R.id.ans)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqAnsAdaptor.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.faq_ans_items, parent, false)

        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: FaqAnsAdaptor.ViewHolder, position: Int) {

        if (data.get(position).listAns != null) {
            viewHolder.ans.text = data.get(position).listAns
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }
}