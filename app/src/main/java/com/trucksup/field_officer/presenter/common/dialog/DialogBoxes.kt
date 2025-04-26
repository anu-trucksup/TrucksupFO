package com.trucksup.field_officer.presenter.common.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.key
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.DutyStatusRequest
import com.trucksup.field_officer.databinding.AddLeadLayoutBinding
import com.trucksup.field_officer.databinding.AddMiscLayoutBinding
import com.trucksup.field_officer.databinding.AttendDialogLayoutBinding
import com.trucksup.field_officer.databinding.CityDialogBinding
import com.trucksup.field_officer.databinding.FilterLayoutBinding
import com.trucksup.field_officer.databinding.MessageDialogLayoutBinding
import com.trucksup.field_officer.databinding.OnOffDutyBinding
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.dashboard.vml.DashBoardViewModel
import com.trucksup.field_officer.presenter.view.adapter.ImageAdapter
import com.trucksup.field_officer.presenter.view.interfaces.AddLeadInterface
import com.trucksup.field_officer.presenter.view.interfaces.AddMiscInterface
import com.trucksup.field_officer.presenter.view.interfaces.SearchLocation
import java.util.Date
import java.util.Locale

object DialogBoxes {

    private val calendar = Calendar.getInstance()

    fun addLeadDialog(context: Context, title: String, addLeadInterface: AddLeadInterface) {
        val builder = AlertDialog.Builder(context)
        val binding = AddLeadLayoutBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        binding.tvTitle.text = title

        addLeadInterface.onLocation(dialog, binding)

        //add lead button
        binding.addLeadButton.setOnClickListener {
            dialog.dismiss()
        }

        //cancel button
        binding.cancelLeadButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun cityState(context: Context, searchLocation: SearchLocation) {
        val builder = AlertDialog.Builder(context)
        val binding = CityDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()

        searchLocation.searchLocation(dialog, binding)

        //back button
        binding.back.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun onOffDuty(
        context: Context,
        dutyStatus: Boolean,
        dashBoardViewModel: DashBoardViewModel?,
        latitude: String?,
        longitude: String?,
        address: String?,
    ) {
    fun onOffDuty(
        context: Context,
        dutyStatus: Boolean,
        dashBoardViewModel: DashBoardViewModel?,
        latitude: String?,
        longitude: String?,
        address: String?
    ) {
        val builder = AlertDialog.Builder(context)
        val binding = OnOffDutyBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //activate button
        binding.btnActivate.setOnClickListener {
            attendanceDialog(context, dutyStatus, dashBoardViewModel, latitude, longitude, address)
            dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun addMiscDisc(context: Context, addMiscInterface: AddMiscInterface) {
        val dialog = BottomSheetDialog(context)
        val binding = AddMiscLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        addMiscInterface.addMisLayout(binding, dialog)

        dialog?.show()
    }

    fun setFilter(context: Context, type: String) {
        val dialog = BottomSheetDialog(context)
        val binding = FilterLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        var selectedKyc: String = ""

        if (type == "ba") {
            binding.tvSP.visibility = View.VISIBLE
            binding.linearLayout2.visibility = View.VISIBLE

            binding.tvED.visibility = View.VISIBLE
            binding.linearLayout4.visibility = View.VISIBLE
        } else {
            binding.tvSP.visibility = View.GONE
            binding.linearLayout2.visibility = View.GONE

            binding.tvED.visibility = View.GONE
            binding.linearLayout4.visibility = View.GONE
        }

        //test
        binding.kycSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedKyc = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        //test

        //expiry date
        binding.expirySpinner.setOnClickListener {
            showDatePicker(context, binding.expirySpinner)
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        //submit button
        binding.submitButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun setFilters(context: Context, type: String, listener: OnFilterValueInputListener) {
        val dialog = BottomSheetDialog(context)
        val binding = FilterLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        var selectedKyc: String = ""
        var selectedVisitType: String = ""

        if (type == "ba") {
            binding.tvSP.visibility = View.VISIBLE
            binding.linearLayout2.visibility = View.VISIBLE

            binding.tvED.visibility = View.VISIBLE
            binding.linearLayout4.visibility = View.VISIBLE
        } else {
            binding.tvSP.visibility = View.GONE
            binding.linearLayout2.visibility = View.GONE

            binding.tvED.visibility = View.GONE
            binding.linearLayout4.visibility = View.GONE
        }

        //add by me
        binding.kycSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedKyc = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.visitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedVisitType = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        //add by me

        //expiry date
        binding.expirySpinner.setOnClickListener {
            showDatePicker(context, binding.expirySpinner)
            //dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        //submit button
        binding.submitButton.setOnClickListener {
            listener.onInput(selectedKyc, selectedVisitType)
            dialog.dismiss()
        }

        dialog.show()

    }

    fun messageDialog(context: Context, msg: String) {
        val builder = AlertDialog.Builder(context)
        val binding = MessageDialogLayoutBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.message.text = msg

        //ok button
        binding.ok.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDatePicker(context: Context, tvDate: TextView) {
        // Create a DatePickerDialog
        val listener =
            DatePickerDialog.OnDateSetListener { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)

                val dateFormat1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                // Update the TextView to display the selected date with the "Selected Date: " prefix
                tvDate.text = "$formattedDate"
            }
        val datePickerDialog = DatePickerDialog(
            context, listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        val minDate = Calendar.getInstance()
        minDate.add(Calendar.YEAR, -1)
        val maxDate = Calendar.getInstance()
//        maxDate.add(Calendar.YEAR, 1)

//        datePickerDialog.datePicker.setMinDate(minDate.getTimeInMillis())
//        datePickerDialog.datePicker.setMaxDate(maxDate.getTimeInMillis())
        datePickerDialog.show()
    }

    private fun attendanceDialog(
        context: Context,
        dutyStatus: Boolean,
        dashBoardViewModel: DashBoardViewModel?,
        latitude: String?,
        longitude: String?,
        address: String?,
    ) {
    private fun attendanceDialog(
        context: Context,
        dutyStatus: Boolean,
        dashBoardViewModel: DashBoardViewModel?,
        latitude: String?,
        longitude: String?,
        address: String?
    ) {
        val builder = AlertDialog.Builder(context)
        val binding = AttendDialogLayoutBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val calendar = Calendar.getInstance()
        val getCurrentDate = SimpleDateFormat("dd-MMM-yy")
        val getCurrentTime = SimpleDateFormat("hh:mm a")
        val currentDate = getCurrentDate.format(calendar.time)
        val currentTime = getCurrentTime.format(Date()).toString()
        val formattedTime = currentTime.replace("am", "AM").replace("pm", "PM");

        binding.tvDate.setText("Date: " + currentDate)
        binding.tvTime.setText("Time: " + formattedTime)

        //ok button
        binding.confirm.setOnClickListener {
            LoadingUtils.showDialog(context, false)

            val request = DutyStatusRequest(
                PreferenceManager.getUserData(context)?.boUserid?.toInt() ?: 0,
                PreferenceManager.getUserData(context)?.boUserid?.toInt() ?: 0,
                dutyStatus,
                latitude ?: "",
                address ?: "",
                longitude ?: "",
                "" + PreferenceManager.getServerDateUtc(),
                PreferenceManager.getRequestNo().toInt(),
                "" + PreferenceManager.getPhoneNo(context)
            )
            dashBoardViewModel?.dutyStatus(request)
            dialog.dismiss()
        }

        //ok button
        binding.cancle.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}

interface OnFilterValueInputListener {
    fun onInput(kycStatus: String, visitType: String)
}
