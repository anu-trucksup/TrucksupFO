package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.BrokerScheduledItemBinding
import com.trucksup.field_officer.presenter.view.activity.other.LoadMeetingActivity

class BrokerScheduled(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<BrokerScheduled.ViewHolder>() {

    class ViewHolder(var binding: BrokerScheduledItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            BrokerScheduledItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
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
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse("tel:" + "9999370747")
//            context?.startActivity(intent)
//            DialogBoxes.scheduledMeeting(context!!,object :ScheduledMeeting{
//                override fun scheduledMeeting(
//                    dialog: AlertDialog,
//                    binding: ScheduledMeetingBinding
//                ) {
//
//                }
//            })
            val intent = Intent(context, LoadMeetingActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}