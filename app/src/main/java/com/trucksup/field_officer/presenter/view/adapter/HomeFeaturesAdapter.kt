package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ItemFeaturesBinding
import com.trucksup.field_officer.presenter.view.activity.business_associate.BAFollowupActivity
import com.trucksup.field_officer.presenter.view.activity.growth_partner.GPFollowupActivity
import com.trucksup.field_officer.presenter.view.activity.profile.TotalAddLoadScreen
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.TSFollowupActivity


class HomeFeaturesAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<HomeFeaturesAdapter.ViewHolder>() {

    val serviceList = arrayListOf(
        "Truck Supplier",
        "Business Associates",
        "Growth Partners",
        "Total Add Loads",
        "Total downloads",
        "Subscription Plans",
        "Finance Leads",
        "Insurance Leads",
        "Smart Fuel Leads"
    )
    val imageList = arrayListOf(
        R.drawable.truck_img,
        R.drawable.ba_ic,
        R.drawable.growth_part,
        R.drawable.load_feature,
        R.drawable.down_feature,
        R.drawable.subscribe_feature,
        R.drawable.finance_feature,
        R.drawable.insure_feature,
        R.drawable.fuel_feature
    )

    inner class ViewHolder(var binding: ItemFeaturesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemFeaturesBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.ivImage.setImageResource(imageList[position])
        holder.binding.tvName.text = serviceList[position]

        holder.binding.root.setOnClickListener {
            //Toast.makeText(context,"Under Development", Toast.LENGTH_SHORT).show()
            when (position) {
                0 -> {
                    val intent = Intent(context, TSFollowupActivity::class.java)
                    context?.startActivity(intent)
                }
                1 -> {
                    val intent = Intent(context, BAFollowupActivity::class.java)
                    context?.startActivity(intent)
                }
                2 -> {
                    val intent = Intent(context, GPFollowupActivity::class.java)
                    context?.startActivity(intent)
                }
                3 -> {
                    val intent = Intent(context, TotalAddLoadScreen::class.java)
                    context?.startActivity(intent)
                }
                4 -> {
                    val intent = Intent(context, GPFollowupActivity::class.java)
                    context?.startActivity(intent)
                }
            }


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}