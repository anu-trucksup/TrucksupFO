package com.trucksup.field_officer.presenter.view.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ItemMyteamGpDetailsBinding
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox

class MyTeamGPDetailsAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<MyTeamGPDetailsAdapter.ViewHolder>() {

    private var controllerListener: ControllerListener? = null

    inner class ViewHolder(var binding: ItemMyteamGpDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemMyteamGpDetailsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tveName.setText("Supriya Tripathi")
        holder.binding.tvAmount.setText(context?.getString(R.string.amount_disbursed))
        holder.binding.tvMobileNo.setText("Mobile No: 9876543215")
        holder.binding.tvRefId.setText("Ref. ID #FIN37-142848PB")

        holder.binding.cardStatus.setOnClickListener{
            if(holder.binding.l2.isVisible){
                holder.binding.l2.visibility = View.GONE
            }else{
                holder.binding.l2.visibility = View.VISIBLE

            }
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