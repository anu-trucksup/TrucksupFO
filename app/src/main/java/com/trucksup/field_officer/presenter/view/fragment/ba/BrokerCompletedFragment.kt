package com.trucksup.field_officer.presenter.view.fragment.ba

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.FragmentBrokerCompletedBinding
import com.trucksup.field_officer.presenter.view.adapter.BrokerCompleted
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes

class BrokerCompletedFragment : Fragment() {

    private lateinit var binding: FragmentBrokerCompletedBinding
    private var aContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            aContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBrokerCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRvList()

        onListeners()
    }

    private fun onListeners() {
        //date picker
        binding.imgCalender.setOnClickListener {
            dateFilterDialog()
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilter(aContext!!, "ba")
        }
    }

    private fun setRvList() {
        var list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rv.apply {
            layoutManager = LinearLayoutManager(aContext, RecyclerView.VERTICAL, false)
            adapter = BrokerCompleted(aContext, list)
            hasFixedSize()
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