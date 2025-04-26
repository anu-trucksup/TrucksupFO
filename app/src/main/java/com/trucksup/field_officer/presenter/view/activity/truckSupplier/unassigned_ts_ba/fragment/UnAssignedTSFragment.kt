package com.trucksup.field_officer.presenter.view.activity.truckSupplier.unassigned_ts_ba.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.FragmentUnassignedTsBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.profile.vml.MyEarningViewModel
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.unassigned_ts_ba.adapter.UnAssignedTSAdapter
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.unassigned_ts_ba.vml.UnAssignedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnAssignedTSFragment : Fragment() {
    private var aContext: Context? = null
    private lateinit var binding: FragmentUnassignedTsBinding
    private var mViewModel: UnAssignedViewModel? = null

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
        binding = FragmentUnassignedTsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this)[UnAssignedViewModel::class.java]

        LoadingUtils.showDialog(aContext, false)
        val request = FollowUpRequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(aContext!!),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(aContext!!)?.boUserid?.toInt() ?: 0
        )
        mViewModel?.getUnAssignedTS(PreferenceManager.getAuthToken(), request)

        setRvList()
        setOnListeners()
        setupObserver()
    }

    private fun setupObserver() {
        mViewModel?.resultUnAssignedBALD?.observe(viewLifecycleOwner) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                LoadingUtils.hideDialog()

                val abx =
                    AlertBoxDialog(
                        requireActivity(),
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                LoadingUtils.hideDialog()

                if (responseModel.success?.statuscode == 200) {
                    setItemList(responseModel.success)
                } else {
                    val abx =
                        AlertBoxDialog(
                            requireActivity(),
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }

    }

    private fun setItemList(success: FollowUpResponse) {
        setRvList()
    }

    private fun setRvList() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rv.apply {
            layoutManager = LinearLayoutManager(aContext, RecyclerView.VERTICAL, false)
            adapter = UnAssignedTSAdapter(aContext, list)
            hasFixedSize()
        }
    }

    private fun setOnListeners() {

        //filter
        binding.imgFilter.setOnClickListener {
            aContext?.let { it1 -> DialogBoxes.setFilter(it1, "owner") }
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

