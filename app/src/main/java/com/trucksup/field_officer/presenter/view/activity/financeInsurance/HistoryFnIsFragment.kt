package com.trucksup.field_officer.presenter.view.activity.financeInsurance

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
import com.trucksup.field_officer.data.model.insurance.FinanceHisAdap
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.databinding.AddScheduledLayoutBinding
import com.trucksup.field_officer.databinding.FragmentActiveBABinding
import com.trucksup.field_officer.databinding.FragmentHistoryInfnsBinding
import com.trucksup.field_officer.presenter.view.adapter.ActiveBAAdapter
import java.util.Calendar


class HistoryFnIsFragment : Fragment() {
    private var aContext: Context? = null
    private lateinit var binding: FragmentHistoryInfnsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            aContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

       // setRvList()

        setListener()

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

}

