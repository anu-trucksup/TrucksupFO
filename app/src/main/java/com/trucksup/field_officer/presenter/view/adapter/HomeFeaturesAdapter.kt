package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.HomeServicesModel
import com.trucksup.field_officer.databinding.ItemFeaturesBinding
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.BAPerformanceActivity
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.FinanceHistoryActivity
import com.trucksup.field_officer.presenter.view.activity.growthPartner.GPPerformanceActivity
import com.trucksup.field_officer.presenter.view.activity.addLoads.TotalAddLoadActivity
import com.trucksup.field_officer.presenter.view.activity.profile.TotalDownloadsActivity
import com.trucksup.field_officer.presenter.view.activity.smartfuel.SmartFuelHistoryActivity
import com.trucksup.field_officer.presenter.view.activity.subscription.SubscriptionActivity
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.TSPerformanceActivity


class HomeFeaturesAdapter(var context: Context?, var list: ArrayList<HomeServicesModel>) :
    RecyclerView.Adapter<HomeFeaturesAdapter.ViewHolder>() {

//    private val serviceList = arrayListOf(
//        "Truck Suppliers",
//        "Business Associates",
//        "Growth Partners",
//        "Total Add Loads",
//        "Total downloads",
//        "Subscription Plans",
//        "Finance Leads",
//        "Insurance Leads",
//        "Smart Fuel Leads"
//    )
//    private val imageList = arrayListOf(
//        R.drawable.truck_img,
//        R.drawable.ba_ic,
//        R.drawable.growth_part,
//        R.drawable.load_feature,
//        R.drawable.down_feature,
//        R.drawable.subscribe_feature,
//        R.drawable.finance_feature,
//        R.drawable.insure_feature,
//        R.drawable.fuel_feature
//    )

    inner class ViewHolder(var binding: ItemFeaturesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemFeaturesBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.ivImage.setImageResource(list[position].serviceImage)
        holder.binding.tvName.text = list[position].service
        holder.binding.tvCount.text = list[position].serviceCount ?: "0"

        holder.binding.root.setOnClickListener {
            //Toast.makeText(context,"Under Development", Toast.LENGTH_SHORT).show()
            when (position) {
                0 -> {
                    val intent = Intent(context, TSPerformanceActivity::class.java)
                    context?.startActivity(intent)
                }

                1 -> {
                    val intent = Intent(context, BAPerformanceActivity::class.java)
                    context?.startActivity(intent)
                }

                2 -> {
                    val intent = Intent(context, GPPerformanceActivity::class.java)
                    context?.startActivity(intent)
                }

                3 -> {
                    val intent = Intent(context, TotalAddLoadActivity::class.java)
                    context?.startActivity(intent)
                }

                4 -> {
                    val intent = Intent(context, TotalDownloadsActivity::class.java)
                    context?.startActivity(intent)
                }

                5 -> {
                    val intent = Intent(context, SubscriptionActivity::class.java)
                    context?.startActivity(intent)
                }

                6 -> {
                    //Finance
                    val intent = Intent(context, FinanceHistoryActivity::class.java)
                    intent.putExtra("HISTORY_TYPE", "Finance")
                    context?.startActivity(intent)
                }

                7 -> {
                    //Insurance
                    val intent = Intent(context, FinanceHistoryActivity::class.java)
                    intent.putExtra("HISTORY_TYPE", "Insurance")
                    context?.startActivity(intent)
                }

                8 -> {
                    val intent = Intent(context, SmartFuelHistoryActivity::class.java)
                    context?.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}