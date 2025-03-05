package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.PlanItemBinding


class PlanAdapter(var context: Context,var list:ArrayList<String>):RecyclerView.Adapter<PlanAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: PlanItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v=PlanItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text=list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}