package com.trucksup.field_officer.presenter.view.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.TsPerformItemBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetMeetScheduleDetailsResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class TSPerformanceAdapter(var context: Context?, var list: ArrayList<GetMeetScheduleDetailsResponse.GetTsdetails>) :
    RecyclerView.Adapter<TSPerformanceAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var filteredList = ArrayList<GetMeetScheduleDetailsResponse.GetTsdetails>()

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    init {
        filteredList.addAll(list) // Initially show all
    }

    inner class ViewHolder(var binding: TsPerformItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = TsPerformItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.binding.highLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.notHighLightView.setBackgroundResource(R.color.transeprant)
        } else {
            holder.binding.notHighLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.highLightView.setBackgroundResource(R.color.transeprant)
        }

        if(list[position].ownerName.isNotEmpty()){
            holder.binding.name.setText(list[position].ownerName)
        }
        if(list[position].mobileNo.isNotEmpty()){
            holder.binding.tvMobile.setText(list[position].mobileNo)
        }
        if(list[position].address.isNotEmpty()){
            holder.binding.address.setText(list[position].address)
        }
        if(list[position].lastMeeting.isNotEmpty()){
            holder.binding.txtLastMeeting.setText("Last Meeting: "+list[position].lastMeeting)
        }
        if(list[position].meetingCount.isNotEmpty()){
            holder.binding.txtMeetingCount.setText("Meeting count : "+list[position].meetingCount)
        }
        if(list[position].distance.isNotEmpty()){
            holder.binding.linDistance.visibility = View.VISIBLE
            holder.binding.tvDistance.setText("Distance: "+list[position].distance+" KM")
        }else{
            holder.binding.linDistance.visibility = View.GONE
        }
        if(list[position].lastCallInitiated.isNotEmpty()){
            holder.binding.txtLastCallInitiated.setText(context?.getString(R.string.last_call_initiated)+" "+list[position].lastCallInitiated)
        }
        if(list[position].visitType.isNotEmpty()){
            holder.binding.tvVisit.setText(list[position].visitType)
        }
        if(list[position].kycStatus.isNotEmpty()){
            if(list[position].kycStatus.equals("Unverified")){
                holder.binding.imgKycStatus.setImageResource(R.drawable.ic_kyc_unverified)
            }
            if(list[position].kycStatus.equals("Verified")){
                holder.binding.imgKycStatus.setImageResource(R.drawable.kyc_done_icon_svg)
            }

        }
        if(list[position].trucksAdded.isNotEmpty()){
            holder.binding.txtTrucksAdded.setText("No. of Trucks Added : "+list[position].trucksAdded)
        }

        list.get(position)
        holder.binding.btnSchedule.setOnClickListener {
            //listener?.onItemClick(position)
            dateFilterDialog(position, list[position].ownerName)
        }


    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFilterDialog(pos: Int, ownerName: String) {
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
                listener?.onItemClick(ownerName, selectedDate, selectedTime)
                dialog.dismiss()
            }


        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(ownerName: String, selectedDate: String, selectedTime : String)
    }

    fun filter(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(list)
        } else {
            filteredList.addAll(
                list.filter { it.mobileNo.contains(query, ignoreCase = true) }

            )
        }
        notifyDataSetChanged()
    }
}