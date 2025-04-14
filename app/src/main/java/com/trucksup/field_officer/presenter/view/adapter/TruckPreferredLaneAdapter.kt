package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.activities.preferre.modle.Preflane
import com.trucksup.field_officer.databinding.TsPreferredLaneItemBinding

class TruckPreferredLaneAdapter(
    var context: Context,
    var list: ArrayList<Preflane>,
    var controllerListener: ControllerListener
) : RecyclerView.Adapter<TruckPreferredLaneAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: TsPreferredLaneItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = TsPreferredLaneItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvLane.text = list[position].fromCity + ">>" + list[position].toCity

        holder.binding.btnDelete.setOnClickListener {
            controllerListener.onDelete(position)
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