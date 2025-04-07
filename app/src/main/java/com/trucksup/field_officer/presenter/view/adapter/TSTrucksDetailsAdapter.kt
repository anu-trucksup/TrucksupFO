package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.AddTruckListItemsBinding
import com.trucksup.field_officer.databinding.AddTsTruckDetailsItemsBinding
import com.trucksup.field_officer.databinding.PreferredLaneItemBinding

class TSTrucksDetailsAdapter(
    var context: Context,
    var list: ArrayList<String>,
    var controllerListener: ControllerListener
) : RecyclerView.Adapter<TSTrucksDetailsAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: AddTsTruckDetailsItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = AddTsTruckDetailsItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.truckNo.text = "UP14FZ7850"
        /*holder.binding.btnDelete.setOnClickListener {
            controllerListener.onDeleteTruck(position)
        }*/
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ControllerListener {
        fun onDeleteTruck(position: Int)
    }

}