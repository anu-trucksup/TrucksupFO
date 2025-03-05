package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ItemFeaturesBinding
import com.trucksup.field_officer.presenter.view.activity.business_associate.BAFollowupActivity
import com.trucksup.field_officer.presenter.view.activity.growth_partner.GPFollowupActivity
import com.trucksup.field_officer.presenter.view.activity.other.MainActivity
import com.trucksup.field_officer.presenter.view.activity.truck_owner.TSFollowupActivity
import com.trucksup.field_officer.presenter.view.activity.truck_owner.TruckSupplierDetailActivity


class HomeFeaturesAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<HomeFeaturesAdapter.ViewHolder>() {

    val serviceList = arrayListOf("Truck Supplier","Business Associates","Channel Partners","Finance", "Insurance", "Smart Fuel", "GPS", "Fast Tag")
    val imageList = arrayListOf(R.drawable.truck_img,R.drawable.ba_ic,R.drawable.growth_part,R.drawable.ic_finanace, R.drawable.ic_insurance, R.drawable.ic_smart_fuel, R.drawable.ic_gps, R.drawable.ic_fasttag)

    inner class ViewHolder(var binding: ItemFeaturesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemFeaturesBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.ivImage.setImageResource(imageList[position])
        holder.binding.tvName.text = serviceList[position]

        holder.binding.root.setOnClickListener {

            if(position == 0){
                val intent = Intent(context, TSFollowupActivity::class.java)
                context?.startActivity(intent)
            }else if(position == 1){
                val intent = Intent(context, BAFollowupActivity::class.java)
                context?.startActivity(intent)
            }else if(position == 2){
                val intent = Intent(context, GPFollowupActivity::class.java)
                context?.startActivity(intent)
            }


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}