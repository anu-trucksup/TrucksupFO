package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.TsScheduledItemBinding
import com.trucksup.field_officer.presenter.view.activity.business_associate.BAStartTripActivity
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.TSStartTripActivity

class BAScheduleFollowupAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<BAScheduleFollowupAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: TsScheduledItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = TsScheduledItemBinding.inflate(LayoutInflater.from(context), parent, false)
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

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, TSStartTripActivity::class.java)
            intent.putExtra("title", "" + context?.resources?.getString(R.string.ba_follow_up))
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}