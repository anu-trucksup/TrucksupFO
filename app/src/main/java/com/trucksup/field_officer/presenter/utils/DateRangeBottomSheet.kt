package com.trucksup.field_officer.presenter.common.btmsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.DialogDateRangeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateRangeBottomSheet(
    private val onDateSelected: (String, String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogDateRangeBinding? = null
    private val binding get() = _binding!!

    private var startMillis: Long? = null
    private var endMillis: Long? = null
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        _binding = DialogDateRangeBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        binding.startConst.setOnClickListener {
            if (binding.tvStartDateValue.text.toString().isNullOrEmpty()) {
                dataSet()
            }
        }

        binding.endConst.setOnClickListener {
            if (binding.tvEndDateValue.text.toString().isNullOrEmpty()) {
                dataSet()
            }
        }

        binding.btnApply.setOnClickListener {
            if (startMillis != null && endMillis != null) {
                val start = formatter.format(Date(startMillis!!))
                val end = formatter.format(Date(endMillis!!))
                onDateSelected(start, end)
                dismiss()
            } else {
                Toast.makeText(context, "Please select a date range", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnClear.setOnClickListener {
            binding.tvStartDateValue.setText("")
            binding.tvEndDateValue.setText("")
            startMillis=null
            endMillis=null

        }

        return dialog
    }

    private fun dataSet()
    {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .setCalendarConstraints(constraints)
            .setTheme(R.style.CustomMaterialCalendarTheme)
            .build()

        picker.show(parentFragmentManager, "DATE_PICKER")

        picker.addOnPositiveButtonClickListener { selection ->
            startMillis = selection.first
            endMillis = selection.second

            val startDate = formatter.format(Date(startMillis!!))
            val endDate = formatter.format(Date(endMillis!!))

            binding.tvStartDateValue.text=startDate
            binding.tvEndDateValue.text=endDate
//                binding.datePreview.text = "StartDate:- $startDate → EndDate:- $endDate"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
