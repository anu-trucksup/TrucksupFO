package com.trucksup.field_officer.presenter.view.fragment.ba

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.AddScheduledLayoutBinding
import com.trucksup.field_officer.databinding.FragmentActiveBABinding
import com.trucksup.field_officer.databinding.FragmentMyTeamBaInactiveBinding
import com.trucksup.field_officer.databinding.FragmentMyTeamTsActiveBinding
import com.trucksup.field_officer.databinding.FragmentMyTeamTsInactiveBinding
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.view.adapter.ActiveBAAdapter
import com.trucksup.field_officer.presenter.view.adapter.MyTeamBAInActiveAdapter
import com.trucksup.field_officer.presenter.view.adapter.MyTeamTSActiveAdapter
import com.trucksup.field_officer.presenter.view.adapter.MyTeamTSInActiveAdapter
import java.util.Calendar


class MyTeamBAInActiveFragment : Fragment() {
    private var aContext: Context? = null
    private lateinit var binding: FragmentMyTeamBaInactiveBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            aContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyTeamBaInactiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRvList()

        setListener()

    }

    private fun setListener() {
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
        binding.rvActiveBA.apply {
            layoutManager = LinearLayoutManager(aContext, RecyclerView.VERTICAL, false)
            adapter = MyTeamBAInActiveAdapter(aContext, list).apply {
                setOnControllerListener(object : MyTeamBAInActiveAdapter.ControllerListener {
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

}

