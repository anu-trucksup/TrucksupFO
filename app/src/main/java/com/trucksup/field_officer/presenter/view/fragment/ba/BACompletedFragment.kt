package com.trucksup.field_officer.presenter.view.fragment.ba

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.FragmentOwnerCompletedBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.view.adapter.TSCompletedAdapter
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.dialog.ProgressDialogBox
import com.trucksup.field_officer.presenter.utils.DateRangeBottomSheet
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.BoVisitDetail
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetUpBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetupBAResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAFollowUpViewModel
import com.trucksup.field_officer.presenter.view.adapter.BACompletedAdapter
import com.trucksup.field_officer.presenter.view.adapter.BAScheduleFollowupAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BACompletedFragment() : Fragment() {

    private var aContext: Context? = null
    private lateinit var binding: FragmentOwnerCompletedBinding

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
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOwnerCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this)[BAFollowUpViewModel::class.java]
        LoadingUtils.showDialog(aContext, false)
        setupObserver()
        onListeners()
    }

    private fun onListeners() {
        //date picker
        binding.imgCalender.setOnClickListener {
            val bottomSheet = DateRangeBottomSheet { start, end ->
                Toast.makeText(context, "Selected: $start → $end", Toast.LENGTH_SHORT).show()
            }
            bottomSheet.show(requireActivity().supportFragmentManager, "DATE_BOTTOM_SHEET")
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilter(aContext!!, "owner")
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObserver() {
        val request = GetAllMeetUpBARequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(aContext as Activity),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(aContext as Activity)?.boUserid?.toInt() ?: 0,
            type = "Completed",
            startDate = "",
            endDate = "",
            visitType = "",
            kycType = ""
        )
        mViewModel?.getAllMeetupBA(PreferenceManager.getAuthToken(), request)

        mViewModel?.getAllMeetUpBAResponseLD?.observe(this@BACompletedFragment) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                LoadingUtils.hideDialog()
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
        if (!getAllMeetupBAResponse.boVisitDetails.isNullOrEmpty()
            && getAllMeetupBAResponse.boVisitDetails.size > 0
        ) {
            getAllMeetupBAResponse.boVisitDetails.forEachIndexed { _, getTSDetailsData ->
                run {
                    binding.rv.visibility = View.VISIBLE
                    binding.l1.visibility = View.VISIBLE
                    binding.noData.visibility = View.GONE
                    getAllBAMeetsList.add(getTSDetailsData)
                }
            }
        } else {
            binding.rv.visibility = View.GONE
            binding.l1.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        }
        binding.rv.layoutManager = LinearLayoutManager(aContext)
        val adapter = BACompletedAdapter(aContext as Activity, getAllBAMeetsList)

        binding.rv.adapter = adapter

        // Add search or filter input
        binding.etSearchFillter.addTextChangedListener {
            adapter.filter(it.toString())
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