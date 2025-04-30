package com.trucksup.field_officer.data.model.insurance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.FRHistoryItemBinding
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.TrackStatusAdap

class FinanceHisAdap(var context: Context, var list: ArrayList<InquiryHistoryResponse.InquiryHistory>
) : RecyclerView.Adapter<FinanceHisAdap.ViewHolder>() {

    inner class ViewHolder(var binding: FRHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceHisAdap.ViewHolder {
        val v = FRHistoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FinanceHisAdap.ViewHolder, position: Int) {
       //  holder.setIsRecyclable(false)
//        holder.binding.cardSelfOther.visibility=View.GONE
        //name
        if (!list[position].name.isNullOrEmpty()) {
            holder.binding.tvName.text = list[position].name
        }

        //enquiry from
        if (!list[position].enquiryFrom.isNullOrEmpty())
        {
            holder.binding.tvEnquiryFrom.text=list[position].enquiryFrom+" Enquiry"
        }

        //call status
        if (!list[position].callStatus.isNullOrEmpty()) {
            holder.binding.tvClose.text = "(" + list[position].callStatus + ")"
        }

        if (list[position].callStatusFlag == "0") {
            holder.binding.tvClose.setTextColor(context.getColor(R.color.blue))
        } else if (list[position].callStatusFlag == "1") {
            holder.binding.tvClose.setTextColor(context.getColor(R.color.green_2))
        } else if (list[position].callStatusFlag == "2") {
            holder.binding.tvClose.setTextColor(context.getColor(R.color.expired_color))
        } else {
            holder.binding.tvClose.setTextColor(context.getColor(R.color.blue))
        }

        //mobile no
        if (!list[position].mobileNumber.isNullOrEmpty()) {
            holder.binding.tvMobileNo.text =
                context.getString(R.string.mobile_no_colon) + list[position].mobileNumber
        }

        //ref. id
        if (!list[position].refNo.isNullOrEmpty()) {
            holder.binding.refId.text =
                context.getString(R.string.ref_id) + " " + list[position].refNo
        }

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
            if (list[position].historyDetails.isNullOrEmpty()) {
                holder.binding.l2.visibility = View.GONE
            } else {
                holder.binding.l2.visibility = View.VISIBLE
                setRvList(list[position].historyDetails, holder, position)
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
        list2: ArrayList<InquiryHistoryResponse.InquiryHistory.HistoryDetail>,
        holder: FinanceHisAdap.ViewHolder, position: Int
    ) {
        holder.binding.rvCurrentStatus.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TrackStatusAdap(context, list2, position).apply {
                setOnClickListener(object : TrackStatusAdap.ControllerListener2 {
                    override fun onClick(position2: Int) {
                        this@FinanceHisAdap.list[position2].isVisible = false
                        this@FinanceHisAdap.notifyItemChanged(position2)
                    }
                })
            }

            hasFixedSize()
        }
    }

}