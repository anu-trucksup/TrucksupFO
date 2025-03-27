package com.trucksup.field_officer.presenter.view.activity.subscription

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.modle.Ans
import com.logistics.trucksup.modle.BrokerPlan
import com.logistics.trucksup.modle.OwnerFaq
import com.trucksup.field_officer.R

class PlanFaqAdaptor(val context: Context, var data: ArrayList<OwnerFaq>) :
    RecyclerView.Adapter<PlanFaqAdaptor.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val openBt: ImageButton = itemView.findViewById<ImageButton>(R.id.openBt)
        val question: TextView = itemView.findViewById<TextView>(R.id.question)
        val ans: TextView = itemView.findViewById<TextView>(R.id.ans)
        val ansLayout: LinearLayout = itemView.findViewById<LinearLayout>(R.id.ansLayout)
        val ansList: RecyclerView = itemView.findViewById(R.id.ansList)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanFaqAdaptor.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.faqs_items, parent, false)

        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: PlanFaqAdaptor.ViewHolder, position: Int) {

        if (data.get(position).question != null) {
            viewHolder.question.text = data.get(position).question
        }
        if (data.get(position).answer != null) {
            viewHolder.ans.text = data.get(position).answer
        }
        if (data.get(position).listAns != null && data.get(position).listAns.size > 0) {
            val adapror =
                FaqAnsAdaptor(context, data.get(position).listAns as ArrayList<Ans>)

            viewHolder.ansList.layoutManager = LinearLayoutManager(context)

            viewHolder.ansList.adapter = adapror
            adapror.notifyDataSetChanged()
        }

        viewHolder.itemView.setOnClickListener {
            if (viewHolder.ansLayout.isVisible == true) {
                viewHolder.openBt.setImageResource(R.drawable.spine_down_arrow_icon)
                viewHolder.ansLayout.visibility = View.GONE
            } else {
                viewHolder.openBt.setImageResource(R.drawable.gps_arrow_up)
                viewHolder.ansLayout.visibility = View.VISIBLE
            }
        }
        viewHolder.openBt.setOnClickListener {
            if (viewHolder.ansLayout.isVisible == true) {
                viewHolder.openBt.setImageResource(R.drawable.spine_down_arrow_icon)
                viewHolder.ansLayout.visibility = View.GONE
            } else {
                viewHolder.openBt.setImageResource(R.drawable.gps_arrow_up)
                viewHolder.ansLayout.visibility = View.VISIBLE
            }
        }

    }


    override fun getItemCount(): Int {
        return data.size
    }
}