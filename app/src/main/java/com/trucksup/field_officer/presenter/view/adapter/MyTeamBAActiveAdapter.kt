package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.ItemActiveBaBinding
import com.trucksup.field_officer.databinding.ItemMyTeamBaActiveBinding
import com.trucksup.field_officer.databinding.ItemMyTeamTsActiveBinding

class MyTeamBAActiveAdapter(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<MyTeamBAActiveAdapter.ViewHolder>() {

    private var controllerListener: ControllerListener? = null

    inner class ViewHolder(var binding: ItemMyTeamBaActiveBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemMyTeamBaActiveBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.binding.llProfile.setOnClickListener{
            if(holder.binding.llDeatil.isVisible){
                holder.binding.llDeatil.visibility = View.GONE
            }else{
                holder.binding.llDeatil.visibility = View.VISIBLE

            }
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