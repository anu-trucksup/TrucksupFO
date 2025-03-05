package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.BrokerFindItemBinding
import java.util.Calendar

class BrokerFind(var context: Context?, var list: ArrayList<String>) :
    RecyclerView.Adapter<BrokerFind.ViewHolder>() {

    private var controllerListener: ControllerListener? = null
    private val calendar = Calendar.getInstance()

    class ViewHolder(var binding: BrokerFindItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = BrokerFindItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == 0) {
            holder.binding.highLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.notHighLightView.setBackgroundResource(R.color.transeprant)
        } else {
            holder.binding.notHighLightView.setBackgroundResource(R.drawable.background2)
            holder.binding.highLightView.setBackgroundResource(R.color.transeprant)
        }

        holder.binding.address.setOnClickListener {
            controllerListener?.onOpenLocation("Faridabad")
        }

        holder.binding.call.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + holder.binding.call.text.toString())
            context?.startActivity(intent)
        }

        holder.binding.btnAddSchedule.setOnClickListener {
            controllerListener?.onDateTime()
        }

        holder.binding.rvPlan.apply {
            var list = ArrayList<String>()
            list.add("Silver Plan")
            list.add("Loads Added: 50")
            list.add("Loads Left: 50")
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = PlanAdapter(context, list)
            hasFixedSize()
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