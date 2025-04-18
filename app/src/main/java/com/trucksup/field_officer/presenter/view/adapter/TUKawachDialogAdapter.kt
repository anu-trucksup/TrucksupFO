package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.HomeServicesModel
import com.trucksup.field_officer.databinding.ItemServicesDialogBinding

class TUKawachDialogAdapter(var context: Context?, var tuKawachList:ArrayList<HomeServicesModel>) :
    RecyclerView.Adapter<TUKawachDialogAdapter.ViewHolder>() {

//    private val serviceList = arrayListOf("Vehicle Tracking","Vehicle Verification", "Driving License"
//    /*,"EMI Check", "Bank Account Verification","UPI Verification"*/)
//    private val imageList = arrayListOf(R.drawable.veh_track, R.drawable.veh_verify, R.drawable.dl_verify
//       /* R.drawable.emi_check, R.drawable.bank_verify, R.drawable.upi_verify*/)


    inner class ViewHolder(var binding: ItemServicesDialogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = ItemServicesDialogBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.ivImage.setImageResource(tuKawachList[position].serviceImage)
        holder.binding.tvTitle.text = tuKawachList[position].service
        holder.binding.tvCount.text= tuKawachList[position].serviceCount?:"0"
        holder.binding.root.setOnClickListener {
           /* val intent = Intent(context, MainActivity::class.java)
            context?.startActivity(intent)*/
        }
    }

    override fun getItemCount(): Int {
        return tuKawachList.size
    }
}