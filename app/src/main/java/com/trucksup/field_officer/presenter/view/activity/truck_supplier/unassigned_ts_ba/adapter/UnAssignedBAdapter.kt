package com.trucksup.field_officer.presenter.view.activity.truck_supplier.unassigned_ts_ba.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ItemMyTeamTsInactiveBinding
import com.trucksup.field_officer.databinding.ItemUnassignedBaBinding
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.TSDetailActivity
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.TSStartTripActivity

class UnAssignedBAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<UnAssignedBAdapter.ViewHolder>() {

    private var controllerListener: ControllerListener? = null

    inner class ViewHolder(var binding: ItemUnassignedBaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemUnassignedBaBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (position == 0) {
            holder.binding.highLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.notHighLightView.setBackgroundResource(R.color.transeprant)
        } else {
            holder.binding.notHighLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.highLightView.setBackgroundResource(R.color.transeprant)
        }

        holder.binding.tvSendHappinessCode.setOnClickListener {
            val happinessCodeBox = HappinessCodeBox(context!! as Activity, context!!.getString(R.string.hapinessCodeUnassignedBaMsg),
                context!!.getString(R.string.EnterHappinessCode), context!!.getString(R.string.resand_sms))
            happinessCodeBox.show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnControllerListener(controllerListener: ControllerListener) {
        this.controllerListener = controllerListener
    }

    interface ControllerListener {
        fun onOpenLocation(location: String)
        fun onDateTime()
    }

}