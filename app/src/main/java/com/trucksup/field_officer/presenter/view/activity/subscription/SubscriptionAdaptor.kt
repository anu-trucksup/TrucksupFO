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


class SubscriptionAdaptor(
    val context: Context,
    var data: ArrayList<Broker>,
    var cantroler: PaySubscribtion
) : RecyclerView.Adapter<SubscriptionAdaptor.ViewHolder>() {
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
    ): SubscriptionAdaptor.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.new_sub_items, parent, false)


        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(viewHolder: SubscriptionAdaptor.ViewHolder, position: Int) {

//        if(data.get(position).clickOn==true){
//            viewHolder.main_cat.setBackgroundResource(R.drawable.sub_slect_card)
//          //  viewHolder.recomanded.visibility=View.VISIBLE
//            cantroler.subClick("0",data[position].buyingPrice.toString(),data[position].categoryID.toString(),data[position].productID.toString(),"1",data.get(position))
//        }else{
//           // viewHolder.recomanded.visibility=View.INVISIBLE
//            viewHolder.main_cat.setBackgroundResource(R.drawable.sub_unslect_card)
//        }
        if (data.get(position).recommended.toString().toLowerCase() == "y") {
            viewHolder.recomanded.visibility = View.VISIBLE
        } else {
            viewHolder.recomanded.visibility = View.GONE
        }

        if (data.get(position).isClick == true) {
            viewHolder.main_cat.setBackgroundResource(R.drawable.sub_slect_card)
            viewHolder.days.setTextColor(context.resources.getColor(R.color.white))
            viewHolder.amount.setTextColor(context.resources.getColor(R.color.white))
            viewHolder.planType.setTextColor(context.resources.getColor(R.color.white))
            viewHolder.amountDay.setTextColor(context.resources.getColor(R.color.white))
            viewHolder.loads.setTextColor(context.resources.getColor(R.color.white))

            cantroler.subClick(
                data.get(position)
            )
        } else {

            viewHolder.main_cat.setBackgroundResource(R.drawable.sub_unslect_card)

            viewHolder.days.setTextColor(context.resources.getColor(R.color.blue))

            viewHolder.amount.setTextColor(context.resources.getColor(R.color.blue))
            viewHolder.planType.setTextColor(context.resources.getColor(R.color.blue))
            viewHolder.amountDay.setTextColor(context.resources.getColor(R.color.blue))
            viewHolder.loads.setTextColor(context.resources.getColor(R.color.blue))

        }
        if (data.get(position).validityDays != null) {

            if (data.get(position).subscriptionName.toString().toLowerCase().contains("welcome")) {
                viewHolder.days?.visibility = View.INVISIBLE
            } else {
                viewHolder.days?.visibility = View.VISIBLE
                if (PreferenceManager.getLanguage(context) == "en") {
                    viewHolder.days.text = "Validity: " + data.get(position).validityDays + " days"
                } else {
                    viewHolder.days.text =
                        "वैधता: " + data.get(position).validityDays.toString() + " दिन"
                }
            }
        }



        if (PreferenceManager.getProfileType(context) == 2) {
            if (data.get(position).mrp != null) {

                viewHolder.amount.text =
                    context.resources.getString(R.string.rs) + " " + data.get(position).mrp


            }
            if (data.get(position).addLoadinPkg != null) {
                if (data.get(position).subscriptionName.toString().toLowerCase()
                        .contains("welcome")
                ) {
                    var day: String = context.resources.getString(R.string.day)
                    viewHolder.loads.visibility = View.VISIBLE
                    viewHolder.loads.text =
                        "" + context.resources.getString(R.string.loads) + " " + data.get(position).addLoadinPkg + "/" + day
                } else {
                    viewHolder.loads.visibility = View.VISIBLE
                    viewHolder.loads.text =
                        "" + context.resources.getString(R.string.loads) + " " + data.get(position).addLoadinPkg + ""
                }

            } else {

                viewHolder.loads.visibility = View.GONE
            }
        } else {
            viewHolder.loads.visibility = View.VISIBLE

            viewHolder.amount.text = context.resources.getString(R.string.free)
            viewHolder.loads.text =
                "(" + context.resources.getString(R.string.unlimitedBenefits) + ")"
        }
//        if (data.get(position).perDayAmount!=null)
//        {
//            viewHolder.amountDay.text="("+ context.resources.getString(R.string.rs) + " "+data.get(position).perDayAmount+"/"+context.resources.getString(R.string.day)+")"
//        }
//        else{
//            viewHolder.amountDay.text=""
//        }
////        viewHolder.planDis.text=data.get(position).description
        if (data.get(position).subscriptionName != null) {
            viewHolder.planType.text = data.get(position).subscriptionName
        }
//
        viewHolder.itemView.setOnClickListener {

            updateData(position, data.get(position))

            //cantroler.subClick("0",data[position].buyingPrice.toString(),data[position].categoryID.toString(),data[position].productID.toString(),"1")
        }


    }

    // Returns
    // the total count of items in the list
    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(position: Int, dataS: Broker) {

        var s: Int = data.size - 1
        for (i in 0..s) {
            if (i == position) {
                var upData: Broker = Broker(
                    data.get(i).actualAmount,
                    data.get(i).planList,
                    data.get(i).createdAt,
                    data.get(i).createdBy,
                    data.get(i).currentPlan,
                    data.get(i).description,
                    data.get(i).disabledPlan,
                    data.get(i).gstPercent,
                    data.get(i).headerId,
                    data.get(i).id,
                    data.get(i).isVisible,
                    data.get(i).mrp,
                    data.get(i).planAmount,
                    data.get(i).recommended,
                    data.get(i).sellingPrice,
                    data.get(i).subType,
                    data.get(i).subscriptionName,
                    data.get(i).tags,
                    data.get(i).userType,
                    data.get(i).validityDays,
                    true,
                    data.get(i).freebies,
                    data.get(i).addLoadinPkg,
                    data.get(i).perDayAmount,
                    data.get(i).discount
                )
                data.set(i, upData)
            } else {
                var upData: Broker = Broker(
                    data.get(i).actualAmount,
                    data.get(i).planList,
                    data.get(i).createdAt,
                    data.get(i).createdBy,
                    data.get(i).currentPlan,
                    data.get(i).description,
                    data.get(i).disabledPlan,
                    data.get(i).gstPercent,
                    data.get(i).headerId,
                    data.get(i).id,
                    data.get(i).isVisible,
                    data.get(i).mrp,
                    data.get(i).planAmount,
                    data.get(i).recommended,
                    data.get(i).sellingPrice,
                    data.get(i).subType,
                    data.get(i).subscriptionName,
                    data.get(i).tags,
                    data.get(i).userType,
                    data.get(i).validityDays,
                    false,
                    data.get(i).freebies,
                    data.get(i).addLoadinPkg,
                    data.get(i).perDayAmount,
                    data.get(i).discount
                )
                data.set(i, upData)
            }
        }

        notifyDataSetChanged()
        cantroler.subClick(
            data.get(position)
        )

    }


}