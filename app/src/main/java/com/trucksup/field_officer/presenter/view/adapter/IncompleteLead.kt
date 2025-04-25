package com.trucksup.fieldofficer.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.IncompleteDropItemBinding
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetAllMiscLeadResponse
import com.trucksup.field_officer.presenter.view.adapter.ICImageAdapter

class IncompleteLead(var context: Context, var list: ArrayList<GetAllMiscLeadResponse.IncompletedLead>) :
    RecyclerView.Adapter<IncompleteLead.ViewHolder>() {

    inner class ViewHolder(var binding: IncompleteDropItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncompleteLead.ViewHolder {
        var v = IncompleteDropItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: IncompleteLead.ViewHolder, position: Int) {

//        holder.binding.btnAddImage.setOnClickListener {
//            onControllerListeners?.incompleteAddImage(holder.binding)
//        }

        holder.binding.tvDate.text=list[position].date?:""

        holder.binding.rvImage.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = ICImageAdapter(context, list[position].truckImageList)
            hasFixedSize()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private var onControllerListeners: OnControllerListeners? = null

    interface OnControllerListeners {
        fun incompleteAddImage(incompleteDropItemBinding: IncompleteDropItemBinding)
    }

    fun setOnControllerListeners(onControllerListeners: OnControllerListeners) {
        this.onControllerListeners = onControllerListeners
    }

}