package com.trucksup.field_officer.presenter.view.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.TsPerformItemBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class TSPerformanceAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<TSPerformanceAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(var binding: TsPerformItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = TsPerformItemBinding.inflate(LayoutInflater.from(context), parent, false)
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

        holder.binding.btnSchedule.setOnClickListener {
            //listener?.onItemClick(position)
            dateFilterDialog(position)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("DefaultLocale")
    private fun dateFilterDialog(pos: Int) {
        val builder = AlertDialog.Builder(context)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        val hour = binding.datePickerStart.hour
        val minute = binding.datePickerStart.minute

        // Create a Calendar object
        val calendars = Calendar.getInstance()
        calendars.set(Calendar.HOUR_OF_DAY, hour)
        calendars.set(Calendar.MINUTE, minute)

        // Format to AM/PM
        val sdfs = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val selectedTime = sdfs.format(calendars.time)

        // Get values from DatePicker
        val day = binding.datePickerEnd.dayOfMonth
        val month = binding.datePickerEnd.month
        val year = binding.datePickerEnd.year

       // Create Calendar object
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        // Format the date
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val selectedDate = sdf.format(calendar.time)

        //test

        //test

        //apply button
        binding.btnApply.setOnClickListener {
            listener?.onItemClick(selectedDate, selectedTime)
            dialog.dismiss()
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