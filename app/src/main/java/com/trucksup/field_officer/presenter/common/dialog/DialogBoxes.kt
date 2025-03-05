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
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.AddLeadLayoutBinding
import com.trucksup.field_officer.databinding.AddMiscLayoutBinding
import com.trucksup.field_officer.databinding.AttendDialogLayoutBinding
import com.trucksup.field_officer.databinding.CityDialogBinding
import com.trucksup.field_officer.databinding.FilterLayoutBinding
import com.trucksup.field_officer.databinding.MessageDialogLayoutBinding
import com.trucksup.field_officer.databinding.OnOffDutyBinding
import com.trucksup.field_officer.presenter.view.adapter.ImageAdapter
import com.trucksup.field_officer.presenter.view.interfaces.AddLeadInterface
import com.trucksup.field_officer.presenter.view.interfaces.AddMiscInterface
import com.trucksup.field_officer.presenter.view.interfaces.SearchLocation
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

    fun onOffDuty(context: Context) {
        val builder = AlertDialog.Builder(context)
        val binding = OnOffDutyBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //activate button
        binding.btnActivate.setOnClickListener {
            attendanceDialog(context)
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

        var categoryType = ArrayList<String>()
        categoryType.add("Owner")
        categoryType.add("Business Associate")
        val arrayAdapter = ArrayAdapter(context, R.layout.simple_text_item, categoryType)
        binding.categorySpinner.setAdapter(arrayAdapter)
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val textView: TextView = view as TextView
//                textView.setPadding(0,0,0,0)
                    val typeface = ResourcesCompat.getFont(context, R.font.bai_jamjuree_medium)
                    textView.setTypeface(typeface)
                    textView.setTextColor(context.resources.getColor(R.color.text_grey))
                    textView.setTextSize(13f)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        var list = ArrayList<Bitmap>()
//        list.add("")
//        list.add("")
//        list.add("")
//        list.add("")
        binding.rvImage.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = ImageAdapter(context, list)
            hasFixedSize()
        }

        binding.btnAddImage.setOnClickListener {
            addMiscInterface.addImage(binding)
        }

        //submit button
        binding.btnSubmit.setOnClickListener {
            dialog.dismiss()
        }

        //submit button 2
        binding.btnSubmit2.setOnClickListener {
            dialog.dismiss()
        }

        //save as draft button
        binding.btnSaveAsDraft.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
    }

    fun setFilter(context: Context, type: String) {
        val dialog = BottomSheetDialog(context)
        val binding = FilterLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

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

        dialog?.show()
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

    private fun attendanceDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val binding = AttendDialogLayoutBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //ok button
        binding.confirm.setOnClickListener {
            dialog.dismiss()
        }

        //ok button
        binding.cancle.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}