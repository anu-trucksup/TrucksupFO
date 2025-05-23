package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ItemEarnBinding

class MyEarningAdapter(var context: Context?/*, var list: ArrayList<String>*/) :
    RecyclerView.Adapter<MyEarningAdapter.ViewHolder>() {

    val serviceList = arrayListOf("Finance", "Insurance", "Smart Fuel", "GPS", "Fast Tag")
    val imageList = arrayListOf(R.drawable.ic_finanace, R.drawable.ic_insurance,
        R.drawable.ic_smart_fuel, R.drawable.ic_gps, R.drawable.ic_fasttag,)

    inner class ViewHolder(var binding: ItemEarnBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemEarnBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ivImg.setImageResource(imageList[position])
        holder.binding.tvTitle.text = serviceList[position]

        holder.binding.root.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}