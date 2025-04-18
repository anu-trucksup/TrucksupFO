package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.trucksup.field_officer.databinding.AddScheduledLayoutBinding
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.GpViewallActivityBinding
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.GPViewAllAdapter
import java.util.Calendar

class GPViewAllActivity : BaseActivity() {

    private lateinit var binding: GpViewallActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GpViewallActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        setRvList()

        setOnListeners()
    }

    private fun setRvList() {
        var list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        binding.rvViewallgp.apply {
            layoutManager = GridLayoutManager(this@GPViewAllActivity,2)
            adapter = GPViewAllAdapter(this@GPViewAllActivity, list).apply {
                setOnControllerListener(object : GPViewAllAdapter.ControllerListener {
                    override fun onOpenLocation(location: String) {
                        // geocodeAddress(location)
//                        DialogBoxes.locationPermission(aContext!!)
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
    }

    private fun setOnListeners() {
        //date picker
        binding.imgCalender.setOnClickListener {
            //dateFilterDialog()
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilter(this@GPViewAllActivity, "owner")
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(this@GPViewAllActivity)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(this@GPViewAllActivity))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        //apply button
        binding.btnApply.setOnClickListener {
            dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

}