package com.trucksup.field_officer.presenter.view.activity.financeInsurance

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
import com.trucksup.field_officer.data.model.insurance.FinanceHisAdap
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.databinding.FragmentHistoryInfnsBinding
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceHistoryViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InquiryHistoryRequest
import dagger.hilt.android.AndroidEntryPoint


private val Any.salesCode: String?
    get() {
      return "ghghgj"
    }

@AndroidEntryPoint
class HistoryFnIsFragment(val status: String) : Fragment() {

    private var aContext: Context? = null
    private lateinit var binding: FragmentHistoryInfnsBinding
    private var mViewModel: FinanceHistoryViewModel? = null
    private var historyType: String? = ""
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            aContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enquiryHistory()

        setupObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryInfnsBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this)[FinanceHistoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        if (status == "active") {
            setRvList(arrayListOf())
        } else if (status == "complete") {
            setRvList(arrayListOf())
        } else if (status == "reject") {
            setRvList(arrayListOf())
        }

        setListener()*/

    }

    private fun setRvList(list2: ArrayList<InquiryHistoryResponse.InquiryHistory>) {
        binding.rv.apply {
            layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = FinanceHisAdap(context, list2)
            hasFixedSize()
        }
    }

    private fun setListener() {
        //add new truck owner button
        /* binding.btnAddTruckOwner.setOnClickListener {
             DialogBoxes.addLeadDialog(aContext!!,"Add Truck Owner",object : AddLeadInterface {
                 override fun onLocation(dialog: AlertDialog, binding: AddLeadLayoutBinding) {
                     binding.tvCurrentLocation.text=activity?.findViewById<TextView>(R.id.addressUpdate)?.text
                 }
             })
         }

         //filter
         binding.imgFilter.setOnClickListener {
             DialogBoxes.setFilter(aContext!!,"owner")
         }*/
    }


    private fun enquiryHistory() {
        LoadingUtils.showDialog(aContext, true)
        val request = InquiryHistoryRequest(
            requestId = PreferenceManager.getRequestNo(),
            requestedBy = PreferenceManager.getPhoneNo(aContext!!),
            requestDatetime = PreferenceManager.getServerDateUtc(""),
            mobilenumber = PreferenceManager.getPhoneNo(aContext!!),
            referralCode = PreferenceManager.getUserData(requireActivity()).salesCode ?: "",
            historyType = historyType!!
        )
        mViewModel?.inquiryHistory(request)
    }

    private fun setupObserver() {
        mViewModel?.resultInquiryHistoryLD?.observe(this@HistoryFnIsFragment) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                LoadingUtils.hideDialog()

                val abx =
                    MyAlartBox(
                        requireActivity(),
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                LoadingUtils.hideDialog()

                if (responseModel.success != null) {

                    inquiryHistorySuccess(responseModel.success)

                } else {

                }
            }
        }
    }

    private fun inquiryHistorySuccess(inquiryHistoryResponse: InquiryHistoryResponse) {
        LoadingUtils.hideDialog()
         if (inquiryHistoryResponse.inquiryHistory.isNullOrEmpty()) {
             binding.rv.visibility = View.GONE
             binding.noData.visibility = View.VISIBLE
         } else {
             binding.rv.visibility = View.VISIBLE
             binding.noData.visibility = View.GONE
             if(inquiryHistoryResponse.inquiryHistory.size > 0){
                 setRvList(inquiryHistoryResponse.inquiryHistory)
             }

         }
    }

}

