package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ItemServicesMainBinding

class ServicesMainAdapter(var context: Context?) :
    RecyclerView.Adapter<ServicesMainAdapter.ViewHolder>() {

    private val serviceList =
        arrayListOf("TU Kawach", "Finance", "Insurance", "Smart Fuel", "GPS", "FASTag")
    private val imageList = arrayListOf(
        R.drawable.ic_kawach, R.drawable.ic_finanace, R.drawable.ic_insurance,
        R.drawable.ic_smart_fuel, R.drawable.ic_gps, R.drawable.ic_fasttag,
    )

    private val itemClickListner: OnItemClickListner = context as OnItemClickListner

    inner class ViewHolder(var binding: ItemServicesMainBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemServicesMainBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.serviceImg.setImageResource(imageList[position])
        holder.binding.serviceName.text = serviceList[position]


        holder.binding.root.setOnClickListener {

            itemClickListner.onItemClick(position)

        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }


}

interface OnItemClickListner {
    fun onItemClick(position: Int)
}