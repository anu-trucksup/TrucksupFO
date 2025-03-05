package com.trucksup.fieldofficer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.DrawerItemBinding
import com.trucksup.field_officer.presenter.view.activity.other.ReportActivity

class NavigationMenuItem(var context: Context, var list: ArrayList<String>) :
    RecyclerView.Adapter<NavigationMenuItem.ViewHolder>() {

    inner class ViewHolder(var binding: DrawerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NavigationMenuItem.ViewHolder {
        var v = DrawerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NavigationMenuItem.ViewHolder, position: Int) {
        if (position == 0) {
            holder.itemView.setOnClickListener {
                var intent = Intent(context, ReportActivity::class.java)
                context.startActivity(intent)
            }
        }

        if (position > 1) {
            holder.binding.cardBack.setBackgroundColor(context.getColor(R.color.nav_not_select))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}