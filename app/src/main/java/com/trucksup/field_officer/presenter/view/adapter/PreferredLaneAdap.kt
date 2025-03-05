package com.trucksup.fieldofficer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.data.model.FromToModel
import com.trucksup.field_officer.databinding.PreferredLaneItemBinding

class PreferredLaneAdap(
    var context: Context,
    var list: ArrayList<FromToModel>,
    var controllerListener: ControllerListener
) : RecyclerView.Adapter<PreferredLaneAdap.ViewHolder>() {

    inner class ViewHolder(var binding: PreferredLaneItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreferredLaneAdap.ViewHolder {
        var v = PreferredLaneItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PreferredLaneAdap.ViewHolder, position: Int) {

        holder.binding.tvLane.text = list[position].from + ">>" + list[position].to

        holder.binding.btnDelete.setOnClickListener {
//            notifyItemRemoved(position)
            controllerListener?.onDelete(position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

//    var controllerListener:ControllerListener?=null
//
//    fun setOnControllerListener(controllerListener:ControllerListener)
//    {
//        this.controllerListener=controllerListener
//    }

    interface ControllerListener {
        fun onDelete(position: Int)
    }
}