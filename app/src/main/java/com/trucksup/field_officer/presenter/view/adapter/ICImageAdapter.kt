package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.MiscImageItemBinding

class ICImageAdapter(var context: Context,var list:ArrayList<Bitmap>):RecyclerView.Adapter<ICImageAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: MiscImageItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v=MiscImageItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.image.setImageBitmap(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}