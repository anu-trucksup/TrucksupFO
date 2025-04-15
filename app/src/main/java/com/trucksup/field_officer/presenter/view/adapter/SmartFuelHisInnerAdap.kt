package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.smartfuel.SmartFuelHistoryResponse
import com.trucksup.field_officer.databinding.CurrentStatusItemBinding

class SmartFuelHisInnerAdap(
    var context: Context,
    var list: ArrayList<SmartFuelHistoryResponse.LeadsHistory.LeadDetails>,
    var position2: Int
) : RecyclerView.Adapter<SmartFuelHisInnerAdap.ViewHolder>() {

    inner class ViewHolder(var binding: CurrentStatusItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartFuelHisInnerAdap.ViewHolder {
        val v = CurrentStatusItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: SmartFuelHisInnerAdap.ViewHolder, position: Int) {
        if (list.size - 1 == position) {
            holder.binding.vLine.visibility = View.GONE

//            Glide.
//            with(context).
//            load(R.drawable.status_no).
//            placeholder(R.drawable.placeholder_image2).
//            error(R.drawable.placeholder_image2).
//            into(holder.binding.image)
//
//            holder.binding.tvTitle.setTextColor(context.getColor(R.color.expired_color))
        }

        if (!list[position].cardStatus.isNullOrEmpty()) {
            holder.binding.tvTitle.text = list[position].cardStatus
        }

        if (!list[position].datetime.isNullOrEmpty()) {
            holder.binding.tvDate.text = list[position].datetime
        }

        if (!list[position].cardStatusFlag.isNullOrEmpty()) {
            if (list[position].cardStatusFlag == "0") {
                Glide.with(context).load(R.drawable.status_yes)
                    .placeholder(R.drawable.placeholder_image2).error(R.drawable.placeholder_image2)
                    .into(holder.binding.image)

                holder.binding.tvTitle.setTextColor(context.getColor(R.color.black))
            } else if (list[position].cardStatusFlag == "1") {
                Glide.with(context).load(R.drawable.status_yes)
                    .placeholder(R.drawable.placeholder_image2).error(R.drawable.placeholder_image2)
                    .into(holder.binding.image)

                holder.binding.tvTitle.setTextColor(context.getColor(R.color.green_4))
            } else if (list[position].cardStatusFlag == "2") {
                Glide.with(context).load(R.drawable.status_no)
                    .placeholder(R.drawable.placeholder_image2).error(R.drawable.placeholder_image2)
                    .into(holder.binding.image)

                holder.binding.tvTitle.setTextColor(context.getColor(R.color.expired_color))
            } else {
                Glide.with(context).load(R.drawable.status_yes)
                    .placeholder(R.drawable.placeholder_image2).error(R.drawable.placeholder_image2)
                    .into(holder.binding.image)

                holder.binding.tvTitle.setTextColor(context.getColor(R.color.black))
            }
        } else {
            Glide.with(context).load(R.drawable.status_yes)
                .placeholder(R.drawable.placeholder_image2).error(R.drawable.placeholder_image2)
                .into(holder.binding.image)

            holder.binding.tvTitle.setTextColor(context.getColor(R.color.black))
        }

        holder.itemView.setOnClickListener {
//            v1.binding.l2.visibility = View.GONE
            controllerListener2?.onClick(position2)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    var controllerListener2: ControllerListener2? = null

    fun setOnClickListener(controllerListener2: ControllerListener2) {
        this.controllerListener2 = controllerListener2
    }

    interface ControllerListener2 {
        fun onClick(position: Int)
    }

}