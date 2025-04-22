package com.trucksup.field_officer.presenter.view.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.GpPerformItemBinding
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.view.adapter.BAPerformanceAdapter.OnItemClickListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class GPPerformanceAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<GPPerformanceAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: GPPerformanceAdapter.OnItemClickListener) {
        this.listener = listener
    }
    inner class ViewHolder(var binding: GpPerformItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = GpPerformItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.binding.highLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.notHighLightView.setBackgroundResource(R.color.transeprant)
        } else {
            holder.binding.notHighLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.highLightView.setBackgroundResource(R.color.transeprant)
        }

        holder.binding.resendVerificationCodeTxt.setOnClickListener {

            val happinessCodeBox = HappinessCodeBox(context!! as Activity, context!!.getString(R.string.hapinessCodeMsg),
                context!!.getString(R.string.EnterHappinessCode), context!!.getString(R.string.resand_sms))
            happinessCodeBox.show()
        }

        holder.binding.tvAddSchedule.setOnClickListener{
            dateFilterDialog(position)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFilterDialog(pos: Int) {
        val builder = AlertDialog.Builder(context)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        var selectedDate: String = ""
        var selectedTime: String = ""

        binding.datePickerStart.setOnTimeChangedListener { _, hourOfDay, minute ->
            val hour = binding.datePickerStart.hour
            val minute = binding.datePickerStart.minute

            // Use Calendar to convert and format
            // Create a Calendar object
            val calendars = Calendar.getInstance()
            calendars.set(Calendar.HOUR_OF_DAY, hour)
            calendars.set(Calendar.MINUTE, minute)

            // Format to AM/PM
            val sdfs = SimpleDateFormat("hh:mm a", Locale.getDefault())
            selectedTime = sdfs.format(calendars.time)
        }


        binding.datePickerEnd.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            // Create a Calendar with the selected date
            // Get values from DatePicker
            val day = binding.datePickerEnd.dayOfMonth
            val month = binding.datePickerEnd.month
            val year = binding.datePickerEnd.year

            // Create Calendar object
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            // Format the date
            //val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            //selectedDate = sdf.format(calendar.time)

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sdf.timeZone = TimeZone.getDefault()
            selectedDate = sdf.format(calendar.time)
        }

        //apply button
        binding.btnApply.setOnClickListener {
            if (selectedTime.isEmpty()) {
                context?.resources?.let { it1 ->
                    LoggerMessage.onSNACK(
                        binding.datePickerStart,
                        it1.getString(R.string.start_time),
                        context!!
                    )
                }
            } else if (selectedDate.isEmpty()) {
                context?.resources?.let { it1 ->
                    LoggerMessage.onSNACK(
                        binding.datePickerStart,
                        it1.getString(R.string.select_date),
                        context!!
                    )
                }
            }else{
                listener?.onItemClick(selectedDate, selectedTime)
                dialog.dismiss()
            }


        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(selectedDate: String, selectedTime : String)
    }
}