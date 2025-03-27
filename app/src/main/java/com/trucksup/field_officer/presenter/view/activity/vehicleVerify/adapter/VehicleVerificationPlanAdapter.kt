package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.VasPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.controller.TrackingPlanCountCantroler
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.DataTrackingPlan

class VehicleVerificationPlanAdapter(
    val context: Context,
    var data: ArrayList<DataTrackingPlan>,
    var cantriler: TrackingPlanCountCantroler
) : RecyclerView.Adapter<VehicleVerificationPlanAdapter.ViewHolder>() {
    var co: Int = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var addButtonLay = itemView.findViewById<LinearLayout>(R.id.addButtonLay)
        val addBut: TextView = itemView.findViewById<TextView>(R.id.addBut)
        var calculateLayout: LinearLayout = itemView.findViewById(R.id.calculateLayout)
        val pluse: ImageButton = itemView.findViewById<ImageButton>(R.id.pluse)
        val minus: ImageButton = itemView.findViewById<ImageButton>(R.id.minus)
        val counter: TextView = itemView.findViewById<TextView>(R.id.counter)
        val amount: TextView = itemView.findViewById<TextView>(R.id.amount)
        val dayOfPrice: TextView = itemView.findViewById(R.id.dayOfPrice)
        val planName: TextView = itemView.findViewById<TextView>(R.id.tvTitle)
        val units: TextView = itemView.findViewById(R.id.units)
        val info: ImageButton = itemView.findViewById(R.id.info)
        val remaningVehical: TextView = itemView.findViewById(R.id.remaningVehical)


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.verifying_plan_item, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int
    ) {


        if (data[position].serviceQty != null && data[position].actualPrice != null) {
            var d: String = context.resources.getString(R.string.per)
            var trs: String = context.resources.getString(R.string.rs)
            viewHolder.dayOfPrice.text =
                trs + " " + data[position].mrp + " " + d + " " + data.get(position).unitText
            viewHolder.planName.text=data.get(position).serviceName
            viewHolder.units.text = data.get(position).unitText
            if (data.get(position).counterDay > 1) {
                if (PreferenceManager.getLanguage(context).toString() == "en") {
                    if (data.get(position).serviceType?.lowercase()=="tracking") {
                        viewHolder.units.text = "Days"
                    }
                    else if (data.get(position).serviceType?.lowercase()=="verification")
                    {
                        viewHolder.units.text = "Trucks"
                    }
                    else if (data.get(position).serviceType?.lowercase()=="dlverification")
                    {
                        viewHolder.units.text = "DL"
                    }
                }
//                else {
//                    viewHolder.units.text = "ट्रक"
//                }
            }
        }

        if (data?.get(position)?.remainingCounts!=null && !TextUtils.isEmpty(data?.get(position)?.remainingCounts.toString().trim())) {
            viewHolder.remaningVehical.text = "* " + data?.get(position)?.remainingCounts
            viewHolder.remaningVehical.visibility = View.VISIBLE
        }
        else{
            viewHolder.remaningVehical.visibility = View.GONE
        }

        if (data.get(position).counterDay == 0) {
            viewHolder.counter.text = data.get(position).serviceQty.toString()

            var trs: String = context.resources.getString(R.string.rs)

//            viewHolder.amount.setText(trs+" "+data.get(position).mrp)
            viewHolder.amount.text = ""
            viewHolder.addButtonLay.visibility = View.VISIBLE
            viewHolder.calculateLayout.visibility = View.GONE
        } else {

            viewHolder.counter.text = data.get(position).counterDay!!.toString()
            var trs: String = context.resources.getString(R.string.rs)
            var r: Int = data.get(position).counterDay!! * data.get(position).mrp!!

            viewHolder.amount.setText(trs + " " + r.toString())

            viewHolder.addButtonLay.visibility = View.GONE
            viewHolder.calculateLayout.visibility = View.VISIBLE

        }

        //     viewHolder.amount.setPaintFlags(viewHolder.amount.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)


        viewHolder.info.setOnClickListener {
            try {
               /* var menu: TruckMenu = TruckMenu
                menu.aboutPlan(
                    context as Activity,
                    viewHolder.info,
                    data[position].serviceName!!,
                    data[position].notes!!
                )*/
            } catch (e: Exception) {

            }
        }




        viewHolder.addBut.setOnClickListener {


            viewHolder.addButtonLay.visibility = View.GONE
            viewHolder.calculateLayout.visibility = View.VISIBLE
            var dataAdd: VasPlan = VasPlan(true, 1, data.get(position).vasId.toString())
            cantriler.ClickAdd(0, "free", dataAdd)
            update(position, 1)


        }

        viewHolder.pluse.setOnClickListener {

            var tt: Int = data.get(position).counterDay!! + 1

            var rs: Int = data.get(position).actualPrice!! * tt

            var trs: String = context.resources.getString(R.string.rs)
            var qty: Int = (data.get(position).mrp!! + tt)


            var dataAdd: VasPlan = VasPlan(true, tt, data.get(position).vasId.toString())
            cantriler.ClickAdd(rs, "free", dataAdd)

            update(position, tt)


        }

        viewHolder.minus.setOnClickListener {
            var cd: Int = viewHolder.counter.text.toString().toInt()
            var pd: Int = data.get(position).counterDay!!
            Log.e("day count", "day count " + pd)

            if (pd == 0) {


                var dataAdd: VasPlan = VasPlan(true, 0, data.get(position).vasId.toString())
                cantriler.ClickAdd((data.get(position).actualPrice!!), "free", dataAdd)


                viewHolder.addButtonLay.visibility = View.VISIBLE
                viewHolder.calculateLayout.visibility = View.GONE


            } else {

                val tt: Int = data.get(position).counterDay!! - 1

                val rs: Int = data.get(position).mrp!! * tt


                var f: String = viewHolder.amount.text.toString().trim()
                    .substring(1, viewHolder.amount.text.toString().trim().length)
                var qty: Int = (data.get(position).mrp!! + tt)


                var dataAdd: VasPlan =
                    VasPlan(true, tt, data.get(position).vasId.toString())
                cantriler.ClickMines(
                    (data.get(position).actualPrice!!),
                    tt.toString(),
                    dataAdd
                )

                update(position, tt)


            }
        }


    }

    fun update(i: Int, day: Int) {
        var d: DataTrackingPlan = DataTrackingPlan(
            data?.get(i)?.actualPrice,
            data?.get(i)?.contractEndDate,
            data?.get(i)?.contractStartDate,
            data?.get(i)?.gstPercent,
            data?.get(i)?.hsnNo,
            data?.get(i)?.imageUrl,
            data?.get(i)?.mrp,
            data?.get(i)?.notes,
            data?.get(i)?.recommended,
            data?.get(i)?.serviceDescriptoin,
            data?.get(i)?.serviceName,
            data?.get(i)?.serviceProvider,
            data?.get(i)?.serviceQty,
            data?.get(i)?.serviceType,
            data?.get(i)?.serviceValidityType,
            data?.get(i)?.unitText,
            data?.get(i)?.vasId,
            day,
            data?.get(i)?.remainingCounts
        )
        data.set(i, d)
        notifyItemChanged(i)
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return data.size
    }
}