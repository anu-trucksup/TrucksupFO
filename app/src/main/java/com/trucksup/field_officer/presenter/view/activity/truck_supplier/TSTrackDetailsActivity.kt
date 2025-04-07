package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.FromToModel
import com.trucksup.field_officer.databinding.ActivityTsTruckDetailsBinding
import com.trucksup.field_officer.databinding.AddNewTruckLayoutBinding
import com.trucksup.field_officer.databinding.PreferredLaneDialogBinding
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.TSTrucksDetailsAdapter
import com.trucksup.field_officer.presenter.view.adapter.TrucksDetailsAdap
import com.trucksup.fieldofficer.adapter.PreferredLaneAdap
import com.trucksup.fieldofficer.adapter.TSTruckDeatilsPreferredLaneAdapter
import kotlin.collections.ArrayList

class TSTrackDetailsActivity : BaseActivity(), PreferredLaneAdap.ControllerListener,
    TrucksDetailsAdap.ControllerListener, TSTrucksDetailsAdapter.ControllerListener,
    TSTruckDeatilsPreferredLaneAdapter.ControllerListener {

    private lateinit var binding: ActivityTsTruckDetailsBinding
    private var preferredLaneList = ArrayList<FromToModel>()
    private var trucksDetailsList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTsTruckDetailsBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)

        setListener()
    }

    private fun setListener() {
        //back button
        binding.ivBack.setOnClickListener {
           onBackPressed()
        }

        //add preferred lane
        binding.btnPreferredLane.setOnClickListener {
            //addPreferredLane(this)
            preferredLaneList.add(
                FromToModel(
                    binding.etFrom.getText().toString(),
                    binding.etTo.getText().toString()
                )
            )
            setRvPreferredLane()
        }

        //add truck details
        binding.btnTrucksDetails.setOnClickListener {
            addNewTruckDialog()
        }


        //submit button
        binding.btnSubmit.setOnClickListener {
            val happinessCodeBox = HappinessCodeBox(
                this, getString(R.string.hapinessCodeMsg),
                getString(R.string.EnterHappinessCode),
                getString(R.string.resand_sms)
            )
            happinessCodeBox.show()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun addPreferredLane(context: Context) {
        val builder = AlertDialog.Builder(context)
        val binding = PreferredLaneDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //submit button
        binding.btnSubmit.setOnClickListener {
            preferredLaneList.add(
                FromToModel(
                    binding.etFrom.getText().toString(),
                    binding.etTo.getText().toString()
                )
            )
            setRvPreferredLane()
            dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setRvPreferredLane() {
        binding.rvPreferredLane.apply {
            layoutManager =
                LinearLayoutManager(this@TSTrackDetailsActivity, RecyclerView.VERTICAL, false)
            adapter = TSTruckDeatilsPreferredLaneAdapter(
                this@TSTrackDetailsActivity,
                preferredLaneList,
                this@TSTrackDetailsActivity
            )
            hasFixedSize()
        }
    }

    private fun setRvTruckDetails() {
        binding.rvTrucksDetails.apply {
            layoutManager =
                LinearLayoutManager(this@TSTrackDetailsActivity, RecyclerView.VERTICAL, false)
            adapter = TSTrucksDetailsAdapter(
                this@TSTrackDetailsActivity,
                trucksDetailsList,
                this@TSTrackDetailsActivity
            )
            hasFixedSize()
        }
    }

    override fun onDelete(position: Int) {
        preferredLaneList.removeAt(position)
        setRvPreferredLane()
    }

    override fun onDeleteTruck(position: Int) {
        trucksDetailsList.removeAt(position)
        setRvTruckDetails()
    }

    private fun addNewTruckDialog() {
        val builder = AlertDialog.Builder(this)
        val binding = AddNewTruckLayoutBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //activate button
        binding.btnAddTruck.setOnClickListener {
            trucksDetailsList.add("")
            setRvTruckDetails()
            dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
