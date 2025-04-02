package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.DrawerItemBinding
import com.trucksup.field_officer.presenter.view.activity.other.NavItems
import com.trucksup.field_officer.presenter.view.activity.profile.MyTargetScreen
import com.trucksup.field_officer.presenter.view.activity.profile.MyTeamScreen

class NavigationMenuItem(var context: Context, var list: ArrayList<NavItems>) :
    RecyclerView.Adapter<NavigationMenuItem.ViewHolder>() {

    inner class ViewHolder(var binding: DrawerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = DrawerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.navIcon.setImageResource(list[position].icon)
        holder.binding.title.text = list[position].title
        holder.binding.subtitle.text = list[position].subtitle

        holder.itemView.setOnClickListener {
             if (position == 0) {
                 val intent = Intent(context, MyTeamScreen::class.java)
                context.startActivity(intent)

            } else if (position == 1) {
                val intent = Intent(context, MyTargetScreen::class.java)
                context.startActivity(intent)
            }
        }
        /*if (position > 1) {
            holder.binding.cardBack.setBackgroundColor(context.getColor(R.color.nav_not_select))
        }*/
    }

    override fun getItemCount(): Int {
        return list.size
    }

}