package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.ItemMyTeamBaInactiveBinding
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.TSDetailActivity

class MyTeamBAInActiveAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<MyTeamBAInActiveAdapter.ViewHolder>() {

    private var controllerListener: ControllerListener? = null

    inner class ViewHolder(var binding: ItemMyTeamBaInactiveBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemMyTeamBaInactiveBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.root.setOnClickListener{
            context?.startActivity(Intent(context, TSDetailActivity::class.java))

        }
        /*holder.binding.rvPlan.apply {
            var list=ArrayList<String>()
            list.add("Silver Plan")
            list.add("Loads Added: 50")
            list.add("Loads Left: 50")
            layoutManager= LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            adapter=PlanAdapter(context,list)
            hasFixedSize()
        }*/
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnControllerListener(controllerListener: ControllerListener) {
        this.controllerListener = controllerListener
    }

    interface ControllerListener {
        fun onOpenLocation(location: String)
        fun onDateTime()
    }

}