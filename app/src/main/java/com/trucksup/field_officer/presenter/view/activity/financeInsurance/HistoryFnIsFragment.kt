package com.trucksup.field_officer.presenter.view.activity.financeInsurance

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
import com.trucksup.field_officer.databinding.FragmentHistoryInfnsBinding
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HistoryFnIsFragment(var status: String="", private var historyList: ArrayList<InquiryHistoryResponse.InquiryHistory>?=null) : Fragment() {
    private var aContext: Context? = null
    private lateinit var binding: FragmentHistoryInfnsBinding

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updatedata(historyList)

        /*
                if (status == "active") {
                    setRvList(arrayListOf())
                } else if (status == "complete") {
                    setRvList(arrayListOf())
                } else if (status == "reject") {
                    setRvList(arrayListOf())
                }



        enquiryHistory()

        setupObserver() */

    }

    private fun setRvList(list2: ArrayList<InquiryHistoryResponse.InquiryHistory>) {
        binding.rv.apply {
            layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = FinanceHisAdap(context, list2)
            hasFixedSize()
        }
    }

    fun updatedata(historyList: ArrayList<InquiryHistoryResponse.InquiryHistory>?) {

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


    /*  private fun enquiryHistory() {
          LoadingUtils.showDialog(aContext, false)
          val request = InquiryHistoryRequest(
              requestId = PreferenceManager.getRequestNo(),
              requestedBy = PreferenceManager.getPhoneNo(aContext!!),
              requestDatetime = PreferenceManager.getServerDateUtc(""),
              mobilenumber = PreferenceManager.getPhoneNo(aContext!!),
              referralCode = "7B4C18",
              historyType = historyType!!
          )
          mViewModel?.inquiryHistory(request)
      }

      private fun setupObserver() {
          mViewModel?.resultInquiryHistoryLD?.observe(viewLifecycleOwner) { responseModel ->                     // login function observe
              if (responseModel.serverError != null) {
                  LoadingUtils.hideDialog()

                  val abx = MyAlartBox(requireActivity(), responseModel.serverError.toString(), "m")
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

          if (inquiryHistoryResponse.inquiryHistory.isNullOrEmpty()) {
              binding.rv.visibility = View.GONE
              binding.noData.visibility = View.VISIBLE
          } else {
              binding.rv.visibility = View.VISIBLE
              binding.noData.visibility = View.GONE
              if (inquiryHistoryResponse.inquiryHistory.size > 0) {
                  setRvList(inquiryHistoryResponse.inquiryHistory)
              }

          }
      }*/

}

