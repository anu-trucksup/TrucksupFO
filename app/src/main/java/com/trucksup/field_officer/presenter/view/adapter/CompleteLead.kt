package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.CompleteLeadItemBinding

class CompleteLead(var context: Context, var list: ArrayList<String>) :
    RecyclerView.Adapter<CompleteLead.ViewHolder>() {

    inner class ViewHolder(var binding: CompleteLeadItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = CompleteLeadItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == 0) {
            holder.binding.businessNameLay.visibility = View.GONE
            holder.binding.truckNumberLay.visibility = View.VISIBLE
        } else {
            holder.binding.businessNameLay.visibility = View.VISIBLE
            holder.binding.truckNumberLay.visibility = View.GONE
        }

        var list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        holder.binding.rvImage.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = CImageAdapter(context, list)
            hasFixedSize()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}