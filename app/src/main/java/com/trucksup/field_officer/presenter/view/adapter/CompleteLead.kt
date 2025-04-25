package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.CompleteLeadItemBinding
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetAllMiscLeadResponse

class CompleteLead(var context: Context, var list: ArrayList<GetAllMiscLeadResponse.IncompletedLead>) :
    RecyclerView.Adapter<CompleteLead.ViewHolder>() {

    inner class ViewHolder(var binding: CompleteLeadItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = CompleteLeadItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list[position].category.lowercase().trim()=="bussinessassociate")
        {
            holder.binding.businessNameLay.visibility = View.VISIBLE
            holder.binding.truckNumberLay.visibility = View.GONE

            holder.binding.tvBusinessName.text=list[position].businessName?:""
            holder.binding.tvCategoryType.text="(Bussiness Associate)"
        }
        else
        {
            holder.binding.businessNameLay.visibility = View.GONE
            holder.binding.truckNumberLay.visibility = View.VISIBLE

            holder.binding.tvTruckNumber.text=list[position].truckNumber?:""
            holder.binding.tvCategoryType.text="(Truck Supplier)"
        }

        //name
        holder.binding.tvName.text=list[position].name?:""

        //mobile no
        holder.binding.tvMobile.text=list[position].mobileNo?:""

        if (list[position].truckImageList.isNullOrEmpty()) {

        }
        else
        {
            holder.binding.rvImage.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = CImageAdapter(context, list[position].truckImageList)
                hasFixedSize()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}