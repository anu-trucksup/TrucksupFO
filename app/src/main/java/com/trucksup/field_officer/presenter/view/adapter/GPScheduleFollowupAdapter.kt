package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.GpScheduledItemBinding
import com.trucksup.field_officer.databinding.TsScheduledItemBinding
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.TSStartTripActivity
import com.trucksup.field_officer.presenter.view.adapter.TSScheduleFollowupAdapter.OnItemClickListener

class GPScheduleFollowupAdapter(var context: Context?, var list: ArrayList<com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.BoVisitDetail>) :
    RecyclerView.Adapter<GPScheduleFollowupAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null
    private var filteredList = ArrayList<com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.BoVisitDetail>()

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    init {
        filteredList.addAll(list) // Initially show all
    }

    inner class ViewHolder(var binding: GpScheduledItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = GpScheduledItemBinding.inflate(LayoutInflater.from(context), parent, false)
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
            holder.binding.tvDistance.setText("-")
        }
        if(list[position].distance.isNotEmpty()){
            //holder.binding.linDistance.visibility = View.VISIBLE
            holder.binding.tvDistance.setText("Distance: "+list[position].distance+" KM")
        }else{
            holder.binding.tvDistance.setText("Distance: "+ "-")
        }
        if(list[position].lastCallInitiated.isNotEmpty()){
            holder.binding.txtLastCallInitiated.setText(context?.getString(R.string.last_call_initiated)+" "+list[position].lastCallInitiated)
        }else {
            holder.binding.txtLastCallInitiated.setText(context?.getString(R.string.last_call_initiated) + "-")
        }
        if(list[position].visitType.isNotEmpty()){
            holder.binding.tvVisit.setText(list[position].visitType)
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
        holder.binding.name.setText(""+list[position].cust_Name)
        holder.binding.txtStartTrip.setOnClickListener {
            listener?.onItemClick(list[position].id)
            val intent = Intent(context, TSStartTripActivity::class.java)
            intent.putExtra("title", "" + context?.resources?.getString(R.string.gp_followup))
            context?.startActivity(intent)
        }

        holder.binding.root.setOnClickListener {

            val intent = Intent(context, TSStartTripActivity::class.java)
            intent.putExtra("title", "" + context?.resources?.getString(R.string.gp_followup))
            intent.putExtra("address", "" /*+ list[position].address*/)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
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