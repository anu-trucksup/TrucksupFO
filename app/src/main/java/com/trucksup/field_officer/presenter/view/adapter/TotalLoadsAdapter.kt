package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.TotalLoadsAddedItemBinding

class TotalLoadsAdapter(var context: Context, var list: ArrayList<String>) :
    RecyclerView.Adapter<TotalLoadsAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: TotalLoadsAddedItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = TotalLoadsAddedItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      /*  holder.binding.tvTitle.text = "BA 1"
        holder.binding.tvSubTitle.text = "2"*/
    }

    override fun getItemCount(): Int {
        return list.size
    }
}