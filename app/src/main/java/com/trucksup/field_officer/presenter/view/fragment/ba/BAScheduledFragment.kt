package com.trucksup.field_officer.presenter.view.fragment.ba

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.FragmentOwnerScheduledBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.BoVisitDetail
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetUpBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetupBAResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAFollowUpViewModel
import com.trucksup.field_officer.presenter.view.adapter.BAScheduleFollowupAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class BAScheduledFragment : Fragment(){

    private var aContext: Context? = null
    private lateinit var binding: FragmentOwnerScheduledBinding

    private var mViewModel: BAFollowUpViewModel? = null
    private var getAllBAMeetsList: java.util.ArrayList<BoVisitDetail> = arrayListOf()

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
        binding = FragmentOwnerScheduledBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this)[BAFollowUpViewModel::class.java]

        LoadingUtils.showDialog(aContext, false)
        setupObserver()
        setOnListeners()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObserver() {
        val request = GetAllMeetUpBARequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(aContext as Activity),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(aContext as Activity)?.boUserid?.toInt() ?: 0,
            type = "Scheduled"
        )
        mViewModel?.getAllMeetupBA(PreferenceManager.getAuthToken(), request)

        mViewModel?.getAllMeetUpBAResponseLD?.observe(this@BAScheduledFragment) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                LoadingUtils.hideDialog()
                //dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        aContext as Activity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                if (responseModel.success?.statuscode == 200) {
                    getAllMeetupBAResponse(responseModel.success)
                    LoadingUtils.hideDialog()
                    // setItemList(responseModel.success)
                } else {
                    LoadingUtils.hideDialog()
                    val abx =
                        AlertBoxDialog(
                            aContext as Activity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }
    }

    private fun getAllMeetupBAResponse(getAllMeetupBAResponse: GetAllMeetupBAResponse) {
        getAllMeetupBAResponse?.boVisitDetails?.forEachIndexed { _, getTSDetailsData ->
            run {
                getAllBAMeetsList.add(getTSDetailsData)
            }
        }

        if(getAllBAMeetsList.size > 0){
            binding.rv.visibility = View.VISIBLE
            binding.l1.visibility = View.VISIBLE
            binding.noData.visibility = View.GONE
        }else{
            binding.rv.visibility = View.GONE
            binding.l1.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        }
        binding.rv.layoutManager = LinearLayoutManager(aContext)
        val adapter = BAScheduleFollowupAdapter(aContext, getAllBAMeetsList)

        adapter.setOnItemClickListener(object : BAScheduleFollowupAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                //dataSubmit()
            }
        })
        binding.rv.adapter = adapter

        // Add search or filter input
        binding.etSearchFillter.addTextChangedListener {
            adapter.filter(it.toString())
        }
    }

    private fun setOnListeners() {
        //date picker
        binding.imgCalender.setOnClickListener {
            dateFilterDialog()
        }

        //filter
        binding.imgFilter.setOnClickListener {
            //test


            //test
            //DialogBoxes.setFilter(aContext!!, "owner")
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


    fun showDateRangePicker(context: Context, onDateSelected: (String, String) -> Unit) {
        val datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .build()

        datePicker.show((context as AppCompatActivity).supportFragmentManager, "DATE_RANGE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val startMillis = selection.first ?: return@addOnPositiveButtonClickListener
            val endMillis = selection.second ?: return@addOnPositiveButtonClickListener

            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val formattedStart = formatter.format(Date(startMillis))
            val formattedEnd = formatter.format(Date(endMillis))

            onDateSelected(formattedStart, formattedEnd)
        }
    }

}