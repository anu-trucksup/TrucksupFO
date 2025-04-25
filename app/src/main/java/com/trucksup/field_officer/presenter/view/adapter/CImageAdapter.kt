package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.MiscImageItemBinding
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetAllMiscLeadResponse

class CImageAdapter(var context: Context, var list: ArrayList<GetAllMiscLeadResponse.IncompletedLead.TruckImage>) :
    RecyclerView.Adapter<CImageAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: MiscImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = MiscImageItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Glide.with(context)
                .load(list[position].imageKey)
                .placeholder(R.drawable.placeholder_image2)
                .error(R.drawable.placeholder_image2)
                .into(holder.binding.image)
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}