package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.PreferredLaneItemBinding

class TrucksDetailsAdap(
    var context: Context,
    var list: ArrayList<String>,
    var controllerListener: ControllerListener
) : RecyclerView.Adapter<TrucksDetailsAdap.ViewHolder>() {

    inner class ViewHolder(var binding: PreferredLaneItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = PreferredLaneItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvLane.text = "UP14FZ7850"
        holder.binding.btnDelete.setOnClickListener {
            controllerListener.onDeleteTruck(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ControllerListener {
        fun onDeleteTruck(position: Int)
    }

}