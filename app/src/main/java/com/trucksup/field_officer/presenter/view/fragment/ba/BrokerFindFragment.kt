package com.trucksup.field_officer.presenter.view.fragment.ba

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.AddLeadLayoutBinding
import com.trucksup.field_officer.databinding.AddScheduledLayoutBinding
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.FragmentBrokerFindBinding
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.SharedPreference
import com.trucksup.field_officer.presenter.view.adapter.BrokerFind
import com.trucksup.field_officer.presenter.view.interfaces.AddLeadInterface
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

class BrokerFindFragment : Fragment(){

    private var aContext: Context? = null
    private lateinit var binding: FragmentBrokerFindBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            aContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBrokerFindBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRvList()

        setListener()
    }

    private fun setListener() {
        //add lead button
        binding.btnAddLead.setOnClickListener {
            DialogBoxes.addLeadDialog(aContext!!,"Add Business Associate",object :
                AddLeadInterface {
                override fun onLocation(dialog: AlertDialog, binding: AddLeadLayoutBinding) {
                    binding.tvCurrentLocation.text=activity?.findViewById<TextView>(R.id.addressUpdate)?.text
                }
            })
        }

        //date picker
        binding.imgCalender.setOnClickListener {
            dateFilterDialog()
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilter(aContext!!,"ba")
        }
    }

    private fun setRvList() {
        var list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rv.apply {
            layoutManager = LinearLayoutManager(aContext, RecyclerView.VERTICAL, false)
            adapter = BrokerFind(aContext, list).apply {
                setOnControllerListener(object : BrokerFind.ControllerListener {
                    override fun onOpenLocation(location: String) {
                        geocodeAddress(location)
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
                            addScheduledSubmit(binding,dialog)
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

    private fun geocodeAddress(address: String) {
        val geocoder = Geocoder(aContext!!)
        try {
            val addresses: List<Address>? = geocoder.getFromLocationName(address, 1)
            if (!addresses.isNullOrEmpty()) {
                val destinationLatitude = addresses[0].latitude
                val destinationLongitude = addresses[0].longitude
                openMapsRoute(destinationLatitude, destinationLongitude)
            } else {
                Toast.makeText(context, "Address not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Address not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMapsRoute(destLat: Double, destLng: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$destLat,$destLng&mode=d")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            aContext?.startActivity(mapIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Google Maps app not installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun postLoadWindowDate(d: Int, dateFormat: String): String {
        val calendar: Calendar = GregorianCalendar()
        val sdf = SimpleDateFormat(dateFormat)
        calendar.add(Calendar.DATE, d)
        val day = sdf.format(calendar.time)
        Log.i("postLoadWindowDate", day)
        return day
    }

    private fun addScheduledSubmit(binding: AddScheduledLayoutBinding,dialog:AlertDialog) {
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(
            binding.datePicker.year,
            binding.datePicker.month,
            binding.datePicker.dayOfMonth,
            binding.timePicker.hour,
            binding.timePicker.minute
        )

        val currentCalendar = Calendar.getInstance()
        if (selectedCalendar.before(currentCalendar)) {
            LoggerMessage.onSNACK(
                binding.userName,
                "Previous date and time cannot be selected",
                aContext!!
            )
        } else {
            val month = binding.datePicker.month + 1
            val dayOfMonth = binding.datePicker.dayOfMonth
            val year = binding.datePicker.year

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val formattedDate = sdf.format(selectedCalendar.time)

            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val displayTime = timeFormat.format(selectedCalendar.time)

            if (SharedPreference().isNextDate(
                    SharedPreference().getDateFormat(
                        "dd",
                        "dd/MM/yyyy",
                        "$dayOfMonth/$month/$year"
                    ).toInt(),
                    SharedPreference().getDateFormat(
                        "MM",
                        "dd/MM/yyyy",
                        "$dayOfMonth/$month/$year"
                    ).toInt(),
                    SharedPreference().getDateFormat(
                        "yyyy",
                        "dd/MM/yyyy",
                        "$dayOfMonth/$month/$year"
                    ).toInt()
                )
            ) {

                val date10: String = SharedPreference().postLoadWindowDate(20, "dd/MM/yyyy")

                if (SharedPreference().isNextDateBetween(
                        SharedPreference().getDateFormat(
                            "dd",
                            "dd/MM/yyyy",
                            "$dayOfMonth/$month/$year"
                        ).toInt(),
                        SharedPreference().getDateFormat(
                            "MM",
                            "dd/MM/yyyy",
                            "$dayOfMonth/$month/$year"
                        ).toInt(),
                        SharedPreference().getDateFormat(
                            "yyyy",
                            "dd/MM/yyyy",
                            "$dayOfMonth/$month/$year"
                        ).toInt(),
                        date10, "dd/MM/yyyy"
                    )
                ) {
                    LoggerMessage.onSNACK(
                        binding.userName,
                        "Sorry you can select date between 20 days",
                        requireContext()
                    )
                } else {
//                        viewModel.addSchedule(
//                            brokerName,
//                            brokerMobileNo,
//                            formattedDate,
//                            displayTime
//                        )
//                        findBinding.recyclerView.scrollToPosition(0)
//                        alertDialog.dismiss()
                    dialog.dismiss()
                }
            } else {
                LoggerMessage.onSNACK(
                    binding.userName,
                    "Please select current date or next date.",
                    requireContext()
                )
            }
        }
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(aContext)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(aContext))
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