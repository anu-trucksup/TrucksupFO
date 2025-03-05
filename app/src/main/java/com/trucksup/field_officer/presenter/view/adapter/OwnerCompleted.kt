package com.trucksup.fieldofficer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.OwnerCompletedItemBinding

class OwnerCompleted(var context:Context,var list:ArrayList<String>):RecyclerView.Adapter<OwnerCompleted.ViewHolder>() {

    inner class ViewHolder(var binding: OwnerCompletedItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnerCompleted.ViewHolder {
        var v=OwnerCompletedItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: OwnerCompleted.ViewHolder, position: Int) {
        if (position==0) {
            holder.binding.highLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.notHighLightView.setBackgroundResource(R.color.transeprant)
        }
        else
        {
            holder.binding.notHighLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.highLightView.setBackgroundResource(R.color.transeprant)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}