package com.trucksup.field_officer.presenter.view.activity.smartfuel

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.data.model.insurance.FinanceHisAdap
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.data.model.smartfuel.SmartFuelHistoryResponse
import com.trucksup.field_officer.databinding.FragmentHistoryInfnsBinding
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceHistoryViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InquiryHistoryRequest
import com.trucksup.field_officer.presenter.view.adapter.SmartFuelHisAdap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistorySmartFuelFragment(var status: String="", private var historyList: ArrayList<SmartFuelHistoryResponse.LeadsHistory>?=null) : Fragment() {

    private var aContext: Context? = null
    private lateinit var binding: FragmentHistoryInfnsBinding
    private var mViewModel: FinanceHistoryViewModel? = null

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
        binding = FragmentHistoryInfnsBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this)[FinanceHistoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateData(historyList)
    }

    private fun setRvList(list2: ArrayList<SmartFuelHistoryResponse.LeadsHistory>) {
        binding.rv.apply {
            layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = SmartFuelHisAdap(context, list2)
            hasFixedSize()
        }
    }

    fun updateData(historyList: ArrayList<SmartFuelHistoryResponse.LeadsHistory>?) {

        if (historyList.isNullOrEmpty()) {
            binding.rv.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        } else {
            binding.rv.visibility = View.VISIBLE
            binding.noData.visibility = View.GONE
            if (historyList.size > 0) {
                setRvList(historyList)
            }

        }
    }

}

