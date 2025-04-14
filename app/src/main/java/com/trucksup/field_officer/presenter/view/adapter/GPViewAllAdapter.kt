package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.ItemGpViewallBinding
import com.trucksup.field_officer.presenter.view.activity.growthPartner.MyTeamGPDetailsActivity

class GPViewAllAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<GPViewAllAdapter.ViewHolder>() {

    private var controllerListener: ControllerListener? = null

    inner class ViewHolder(var binding: ItemGpViewallBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemGpViewallBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        /*holder.binding.rvPlan.apply {
            var list=ArrayList<String>()
            list.add("Silver Plan")
            list.add("Loads Added: 50")
            list.add("Loads Left: 50")
            layoutManager= LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            adapter=PlanAdapter(context,list)
            hasFixedSize()
        }*/

        holder.binding.root.setOnClickListener{
            val intent = Intent(context, MyTeamGPDetailsActivity::class.java)
            context?.startActivity(intent)
        }
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