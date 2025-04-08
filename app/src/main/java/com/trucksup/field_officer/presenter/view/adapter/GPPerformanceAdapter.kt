package com.trucksup.field_officer.presenter.view.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.GpPerformItemBinding
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox

class GPPerformanceAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<GPPerformanceAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: GpPerformItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = GpPerformItemBinding.inflate(LayoutInflater.from(context), parent, false)
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

        holder.binding.tvAddSchedule.setOnClickListener{
            dateFilterDialog()
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(context)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(context))
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