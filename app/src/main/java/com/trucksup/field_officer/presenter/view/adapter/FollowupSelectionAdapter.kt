package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ItemFeaturesBinding
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.BAFollowupActivity
import com.trucksup.field_officer.presenter.view.activity.growthPartner.GPFollowupActivity
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.TSFollowupActivity


class FollowupSelectionAdapter(var context: Context?) :
    RecyclerView.Adapter<FollowupSelectionAdapter.ViewHolder>() {

    private val serviceList = arrayListOf(
        "Truck Supplier",
        "Business Associates",
        "Growth Partners"
    )
    private val imageList = arrayListOf(
        R.drawable.truck_img,
        R.drawable.ba_ic,
        R.drawable.growth_part
    )

    inner class ViewHolder(var binding: ItemFeaturesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemFeaturesBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.ivImage.setImageResource(imageList[position])
        holder.binding.tvName.text = serviceList[position]
       // holder.binding.tvCount.text = serviceList[position]

        holder.binding.root.setOnClickListener {
            //Toast.makeText(context,"Under Development", Toast.LENGTH_SHORT).show()
            if (position == 0) {
                val intent = Intent(context, TSFollowupActivity::class.java)
                context?.startActivity(intent)
            } else if (position == 1) {
                val intent = Intent(context, BAFollowupActivity::class.java)
                context?.startActivity(intent)
            } else if (position == 2) {
                val intent = Intent(context, GPFollowupActivity::class.java)
                context?.startActivity(intent)
            }


        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}