package com.trucksup.field_officer.presenter.view.activity.commit

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.PrevCommitItemBinding
import com.trucksup.field_officer.databinding.TotalLoadsAddedItemBinding
import com.trucksup.field_officer.presenter.view.activity.addLoads.TotalAddDetailsActivity

class AllCommitAdapter(var context: Context, var list: ArrayList<String>) :
    RecyclerView.Adapter<AllCommitAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: PrevCommitItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = PrevCommitItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*  holder.binding.tvTitle.text = "BA 1"
          holder.binding.tvSubTitle.text = "2"*/
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}