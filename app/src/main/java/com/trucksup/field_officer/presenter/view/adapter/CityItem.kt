package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.CityLocationItemsBinding

class CityItem(var context: Context, var list: ArrayList<String>) :
    RecyclerView.Adapter<CityItem.ViewHolder>() {

    inner class ViewHolder(var binding: CityLocationItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = CityLocationItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.cityName.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}