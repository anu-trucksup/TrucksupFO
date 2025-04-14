package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.TotalLoadsItemBinding

class TotalLoadsAdapter2(var context: Context, var list:ArrayList<String>):RecyclerView.Adapter<TotalLoadsAdapter2.ViewHolder>() {

    inner class ViewHolder(var binding:TotalLoadsItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TotalLoadsAdapter2.ViewHolder {
        var v=TotalLoadsItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: TotalLoadsAdapter2.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }
}