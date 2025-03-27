package com.trucksup.field_officer.presenter.view.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.TsPerformItemBinding
import com.trucksup.field_officer.databinding.TsScheduledItemBinding
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.TSStartTripActivity

class TSPerformanceAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<TSPerformanceAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: TsPerformItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = TsPerformItemBinding.inflate(LayoutInflater.from(context), parent, false)
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

        holder.binding.resendVerificationCodeTxt.setOnClickListener {

            val happinessCodeBox = HappinessCodeBox(context!! as Activity, context!!.getString(R.string.hapinessCodeMsg),
                context!!.getString(R.string.EnterHappinessCode), context!!.getString(R.string.resand_sms))
            happinessCodeBox.show()
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}