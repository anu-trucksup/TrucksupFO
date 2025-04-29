package com.trucksup.field_officer.presenter.view.activity.subscription

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logistics.trucksup.modle.BrokerPlan
import com.trucksup.field_officer.R


class PlanDetailsAdapter (val context: Context, var data :ArrayList<BrokerPlan>) : RecyclerView.Adapter<PlanDetailsAdapter.ViewHolder>()
{

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById<ImageView>(R.id.image)
        val freeImage: ImageView = itemView.findViewById<ImageView>(R.id.freeImage)
        val icon:ImageButton = itemView.findViewById<ImageButton>(R.id.icon)
        val planText: TextView = itemView.findViewById<TextView>(R.id.planText)
        val planDis: TextView = itemView.findViewById<TextView>(R.id.planDis)
        val planText1: TextView = itemView.findViewById<TextView>(R.id.planText_1)
        val planDis1: TextView = itemView.findViewById<TextView>(R.id.planDis_1)

        val hideLayot: LinearLayout = itemView.findViewById<LinearLayout>(R.id.hideLayot)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanDetailsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.plan_details_items, parent, false)

        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: PlanDetailsAdapter.ViewHolder, position: Int) {

        if (data.get(position).isIncluded!=null&& data.get(position).isIncluded=="N")
        {
            viewHolder.hideLayot.visibility=View.VISIBLE
            viewHolder.icon.visibility=View.INVISIBLE
            viewHolder.freeImage.visibility=View.GONE
            viewHolder.planText.setTextColor(context.resources.getColor(R.color.disable_text_color))
            viewHolder.planText1.setTextColor(context.resources.getColor(R.color.disable_text_color))
            viewHolder.planDis.setTextColor(context.resources.getColor(R.color.disable_text_color))
            viewHolder.planDis1.setTextColor(context.resources.getColor(R.color.disable_text_color))
        }
        else{
            viewHolder.hideLayot.visibility=View.INVISIBLE
            if (data.get(position).isPaid.toString().toLowerCase()=="n")
            {
                viewHolder.freeImage.visibility=View.VISIBLE
                viewHolder.icon.visibility=View.INVISIBLE
            }
            else{
                viewHolder.freeImage.visibility=View.GONE
                viewHolder.icon.visibility=View.VISIBLE
            }

        }


        if (data.get(position).imageurl!=null && !TextUtils.isEmpty(data.get(position).imageurl))
        {
            Glide.with(context)
                .load(data.get(position).imageurl)
                .into(viewHolder.image)
        }
       if (data.get(position).moduleName!=null)
       {
           viewHolder.planText.text=data.get(position).matrixName
           viewHolder.planText1.text=data.get(position).matrixName
       }
        if (data.get(position).matrixName!=null)
        {
            viewHolder.planDis.text=data.get(position).matrixDescription
            viewHolder.planDis1.text=data.get(position).matrixDescription
        }

    }


    override fun getItemCount(): Int {
        return 4
    }
}