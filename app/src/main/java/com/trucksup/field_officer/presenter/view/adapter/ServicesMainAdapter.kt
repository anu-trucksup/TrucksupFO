package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.HomeServicesModel
import com.trucksup.field_officer.databinding.ItemServicesMainBinding

class ServicesMainAdapter(var context: Context?, var serviceList:ArrayList<HomeServicesModel>) :
    RecyclerView.Adapter<ServicesMainAdapter.ViewHolder>() {

//    private val serviceList = arrayListOf("TU Kawach", "Finance", "Insurance", "Smart Fuel", "GPS", "FASTag")
//    private val imageList = arrayListOf(R.drawable.ic_kawach, R.drawable.ic_finanace, R.drawable.ic_insurance, R.drawable.ic_smart_fuel, R.drawable.ic_gps, R.drawable.ic_fasttag,)

    private val itemClickListener: OnItemClickListener = context as OnItemClickListener

    inner class ViewHolder(var binding: ItemServicesMainBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemServicesMainBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.serviceImg.setImageResource(serviceList[position].serviceImage)
        holder.binding.serviceName.text = serviceList[position].service?:""
        holder.binding.tvCount.text=serviceList[position].serviceCount?:"0"

        if (position == 5 || position == 4) {
            holder.binding.countLayout.visibility=View.GONE
            holder.binding.materialCardView.setCardForegroundColor(context!!.getColorStateList(R.color.grey4))
            holder.binding.serviceImg.isEnabled = false
        }

        holder.binding.root.setOnClickListener {
            itemClickListener.onItemClick(position,serviceList[position].serviceCount?:"0")
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}

interface OnItemClickListener {
    fun onItemClick(position: Int,dataCount:String)
}