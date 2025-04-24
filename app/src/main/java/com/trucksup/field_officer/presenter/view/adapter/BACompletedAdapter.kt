package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.BaCompletedFollowupItemBinding
import com.trucksup.field_officer.databinding.TsCompletedItemBinding
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.BoVisitDetail
import com.trucksup.field_officer.presenter.view.adapter.BAScheduleFollowupAdapter.OnItemClickListener

class BACompletedAdapter(var context: Context, var list: ArrayList<BoVisitDetail>) :
    RecyclerView.Adapter<BACompletedAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null
    private var filteredList = ArrayList<BoVisitDetail>()

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    init {
        filteredList.addAll(list) // Initially show all
    }

    inner class ViewHolder(var binding: BaCompletedFollowupItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = BaCompletedFollowupItemBinding.inflate(LayoutInflater.from(context), parent, false)
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
        holder.binding.name.setText(""+list[position].cust_Name)


        if(list[position].scheduleDate.isNotEmpty()){
            holder.binding.txtScheduleDate.setText(list[position].scheduleDate)
        }else{
            holder.binding.txtScheduleDate.setText("-")
        }
        if(list[position].cust_Name.isNotEmpty()){
            holder.binding.name.setText(list[position].cust_Name)
        }
        if(list[position].cust_MobileNo.isNotEmpty()){
            holder.binding.tvMobile.setText(list[position].cust_MobileNo)
        }
        if(list[position].address.isNotEmpty()){
            holder.binding.address.setText(list[position].address)
        }else{
            holder.binding.address.setText("-")
        }
        if(list[position].brokerStatus.isNotEmpty()){
            if(list[position].brokerStatus.equals("Unverified")){
                holder.binding.imgKycStatus.setImageResource(R.drawable.ic_kyc_unverified)
            }
            if(list[position].brokerStatus.equals("Verified")){
                holder.binding.imgKycStatus.setImageResource(R.drawable.kyc_done_icon_svg)
            }

        }
        if(list[position].addedTrucks.isNotEmpty()){
            holder.binding.txtTrucksAdded.setText("No. of Trucks Added : "+list[position].addedTrucks)
        }

    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    interface OnItemClickListener {
        fun onItemClick(ownerName: String, selectedDate: String, selectedTime : String)
    }

    fun filter(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(list)
        } else {
            filteredList.addAll(
                list.filter { it.cust_MobileNo.contains(query, ignoreCase = true) }

            )
        }
        notifyDataSetChanged()
    }
}