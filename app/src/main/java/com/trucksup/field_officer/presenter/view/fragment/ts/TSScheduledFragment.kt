package com.trucksup.field_officer.presenter.view.fragment.ts

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.FragmentOwnerScheduledBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.dialog.OnFilterValueInputListener
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetUpTSResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetupTSRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.TSFollowUpViewModel
import com.trucksup.field_officer.presenter.view.adapter.TSScheduleFollowupAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TSScheduledFragment : Fragment() {

    private var aContext: Context? = null
    private lateinit var binding: FragmentOwnerScheduledBinding
    private var mViewModel: TSFollowUpViewModel? = null
    private var getAllTSMeetsList: ArrayList<com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.BoVisitDetail> =
        arrayListOf()


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

        mViewModel = ViewModelProvider(this)[TSFollowUpViewModel::class.java]
        LoadingUtils.showDialog(aContext, false)


        setupObserver("","")
        setOnListeners()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObserver(visitType:String, kycType:String) {
        val request = GetAllMeetupTSRequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(aContext as Activity),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(aContext as Activity)?.boUserid?.toInt() ?: 0,
            type = "Scheduled",
            startDate = "",
            endDate = "",
            visitType = "",
            kycType = ""
        )
        mViewModel?.getAllMeetupTS(PreferenceManager.getAuthToken(), request)

        mViewModel?.getAllMeetUpTSResponseLD?.observe(this@TSScheduledFragment) { responseModel ->                     // login function observe
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
                    getAllTSMeetsList.clear()
                    getAllMeetupTSResponse(responseModel.success)
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

    private fun getAllMeetupTSResponse(getAllMeetupTSResponse: GetAllMeetUpTSResponse) {
        if (!getAllMeetupTSResponse.boVisitDetails.isNullOrEmpty()
            && getAllMeetupTSResponse.boVisitDetails.size > 0
        ) {
            getAllMeetupTSResponse.boVisitDetails.forEachIndexed { _, getTSDetailsData ->
                run {
                    binding.rv.visibility = View.VISIBLE
                    binding.l1.visibility = View.VISIBLE
                    binding.noData.visibility = View.GONE
                    getAllTSMeetsList.add(getTSDetailsData)
                }
            }
        } else {
            binding.rv.visibility = View.GONE
            binding.l1.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        }
        binding.rv.layoutManager = LinearLayoutManager(aContext)
        val adapter = TSScheduleFollowupAdapter(aContext, getAllTSMeetsList)

        adapter.setOnItemClickListener(object : TSScheduleFollowupAdapter.OnItemClickListener {
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

    private fun setRvList() {
        /*val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rv.apply {
            layoutManager = LinearLayoutManager(aContext, RecyclerView.VERTICAL, false)
            adapter = TSScheduleFollowupAdapter(aContext, list)
            hasFixedSize()
        }*/
    }

    private fun setOnListeners() {
        //date picker
        binding.imgCalender.setOnClickListener {
            dateFilterDialog()
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilters(aContext as Activity, "owner", object :
                OnFilterValueInputListener {
                override fun onInput(kycStatus: String, visitType: String) {
                    setupObserver(visitType, kycStatus)
                    //Toast.makeText(aContext as Activity, "KycStatus: $kycStatus, VisitType: $visitType", Toast.LENGTH_SHORT).show()
                }
            })
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

}