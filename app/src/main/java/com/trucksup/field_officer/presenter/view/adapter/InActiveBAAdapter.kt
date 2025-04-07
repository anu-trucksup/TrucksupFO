package com.trucksup.field_officer.presenter.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.AddScheduledLayoutBinding
import com.trucksup.field_officer.databinding.ItemActiveBaBinding
import com.trucksup.field_officer.databinding.ItemInactiveBaBinding
import com.trucksup.field_officer.databinding.ListDialogBinding
import java.util.Calendar

class InActiveBAAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<InActiveBAAdapter.ViewHolder>() {

    private var controllerListener: ControllerListener? = null

    inner class ViewHolder(var binding: ItemInactiveBaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemInactiveBaBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        /*holder.binding.rvPlan.apply {
            var list=ArrayList<String>()
            list.add("Silver Plan")
            list.add("Loads Added: 50")
            list.add("Loads Left: 50")
            layoutManager= LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            adapter=PlanAdapter(context,list)
            hasFixedSize()
        }*/
        holder.binding.tvViewHistroy.setOnClickListener{
            context?.let { it1 -> SubscriptionPurchaseHistoryDialog(it1) }
        }
        holder.binding.llProfile.setOnClickListener{
            if(holder.binding.llDeatil.isVisible){
                holder.binding.llDeatil.visibility = View.GONE
            }else{
                holder.binding.llDeatil.visibility = View.VISIBLE

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnControllerListener(controllerListener: ControllerListener) {
        this.controllerListener = controllerListener
    }

    interface ControllerListener {
        fun onOpenLocation(location: String)
        fun onDateTime()
    }

    private fun SubscriptionPurchaseHistoryDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val binding = ListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        var list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rvSecondServices.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = ListDialogAdapter(context, list).apply {
                setOnControllerListener(object : InActiveBAAdapter.ControllerListener {
                    override fun onOpenLocation(location: String) {
                    }
                    override fun onDateTime() {
                        val builder = AlertDialog.Builder(context)
                        val binding =
                            AddScheduledLayoutBinding.inflate(LayoutInflater.from(context))
                        builder.setView(binding.root)
                        val dialog: AlertDialog = builder.create()
                        dialog.show()

                        val today = Calendar.getInstance()
                        binding.datePicker.init(
                            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                            today.get(Calendar.DAY_OF_MONTH)
                        ) { _, year, months, day ->
                            val month = months + 1
                            val msg = "$day/$month/$year"
//                            date = msg
                        }

                        binding.timePicker.setOnTimeChangedListener { _, hours, minute ->
                            var hour = hours
                            val amPm: String
                            when {
                                hour == 0 -> {
                                    hour += 12
                                    amPm = "AM"
                                }

                                hour == 12 -> amPm = "PM"
                                hour > 12 -> {
                                    hour -= 12
                                    amPm = "PM"
                                }

                                else -> amPm = "AM"
                            }

                            val hourFormatted = if (hour < 10) "0$hour" else hour
                            val minuteFormatted = if (minute < 10) "0$minute" else minute
                            val msg = "$hourFormatted : $minuteFormatted $amPm"
//                            time = msg
                        }

                        binding.btnSubmit.setOnClickListener {
                            // addScheduledSubmit(binding,dialog)
                        }

                        //cancel button
                        binding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                })
            }
            hasFixedSize()
        }

        dialog.show()

        //cancel button
        binding.imgCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

}