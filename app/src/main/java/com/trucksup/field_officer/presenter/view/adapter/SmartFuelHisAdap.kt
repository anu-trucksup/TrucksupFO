package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.smartfuel.SmartFuelHistoryResponse
import com.trucksup.field_officer.databinding.FRHistoryItemBinding

class SmartFuelHisAdap(var context: Context, var list: ArrayList<SmartFuelHistoryResponse.LeadsHistory>
) : RecyclerView.Adapter<SmartFuelHisAdap.ViewHolder>() {

    inner class ViewHolder(var binding: FRHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartFuelHisAdap.ViewHolder {
        val v = FRHistoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: SmartFuelHisAdap.ViewHolder, position: Int) {
        //  holder.setIsRecyclable(false)
        holder.binding.cardSelfOther.visibility=View.GONE
        //name
        if (!list[position].customerName.isNullOrEmpty()) {
            holder.binding.tvName.text = list[position].customerName
        }

        //call status
        if (!list[position].cardStatus.isNullOrEmpty()) {
            holder.binding.tvClose.text = "(" + list[position].cardStatus + ")"
        }

        if (list[position].cardStatusFlag == "0") {
            holder.binding.tvClose.setTextColor(context.getColor(R.color.blue))
        } else if (list[position].cardStatusFlag == "1") {
            holder.binding.tvClose.setTextColor(context.getColor(R.color.green_2))
        } else if (list[position].cardStatusFlag == "2") {
            holder.binding.tvClose.setTextColor(context.getColor(R.color.expired_color))
        } else {
            holder.binding.tvClose.setTextColor(context.getColor(R.color.blue))
        }

        //mobile no
        if (!list[position].customerRegisteredMobileNo.isNullOrEmpty()) {
            holder.binding.tvMobileNo.text =
                context.getString(R.string.mobile_no_colon) + list[position].customerRegisteredMobileNo
        }

        //ref. id
//        if (!list[position].refNo.isNullOrEmpty()) {
//            holder.binding.refId.text =
//                context.getString(R.string.ref_id) + " " + list[position].refNo
//        }

        //self or other
//        if (list[position].inquiryBy.lowercase() == "self") {
//            holder.binding.cardSelfOther.setCardBackgroundColor(context.getColor(R.color.ins_self))
//            holder.binding.tvSelfOther.text = context.getString(R.string.self)
//        } else if (list[position].inquiryBy.lowercase() == "other") {
//            holder.binding.cardSelfOther.setCardBackgroundColor(context.getColor(R.color.ins_other))
//            holder.binding.tvSelfOther.text = context.getString(R.string.others)
//        }

        //image
        try {
            Glide.with(context)
                .load(list[position].profilePhoto)
                .placeholder(R.mipmap.user_profile)
                .error(R.mipmap.user_profile).into(holder.binding.image)
        } catch (_: Exception) {
        }

        if (list[position].isVisible == false) {
            holder.binding.l2.visibility = View.GONE
        } else {
            if (list[position].leadDetails.isNullOrEmpty()) {
                holder.binding.l2.visibility = View.GONE
            } else {
                holder.binding.l2.visibility = View.VISIBLE
                setRvList(list[position].leadDetails, holder, position)
            }
        }

        holder.binding.card.setOnClickListener {
            if (list[position].isVisible == false) {
                list[position].isVisible = true
            } else {
                list[position].isVisible = false
            }
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setRvList(
        list2: ArrayList<SmartFuelHistoryResponse.LeadsHistory.LeadDetails>,
        holder: SmartFuelHisAdap.ViewHolder, position: Int
    ) {
        holder.binding.rvCurrentStatus.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = SmartFuelHisInnerAdap(context, list2, position).apply {
                setOnClickListener(object : SmartFuelHisInnerAdap.ControllerListener2 {
                    override fun onClick(position2: Int) {
                        this@SmartFuelHisAdap.list[position2].isVisible = false
                        this@SmartFuelHisAdap.notifyItemChanged(position2)
                    }
                })
            }

            hasFixedSize()
        }
    }

}