package com.trucksup.field_officer.presenter.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ImageItemBinding


class SelectedImageAdapter(joblists: List<String>?) :
    RecyclerView.Adapter<SelectedImageAdapter.ViewHolder>() {

    // var deleteListener = context
    var list: List<String>? = joblists

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = DataBindingUtil.inflate<ImageItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.image_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val propertyItem = list?.get(position)
        propertyItem?.let {
            holder.bind(it)
            if (list != null && list!!.size > 0) {
                // loadImageQr(context,holder.itemLayoutBinding.ivImg,list!![position])
            }
        }

        holder.itemLayoutBinding.deleteIcon.setOnClickListener {
            //  deleteListener.ondelete(position)
        }

    }

    override fun getItemCount(): Int {
        if (this.list != null && list!!.size > 0) return list!!.size
        return 0
    }

    inner class ViewHolder(var itemLayoutBinding: ImageItemBinding) :
        RecyclerView.ViewHolder(itemLayoutBinding.root) {

        fun bind(item: String?) {

        }
    }

    interface DeleteListener {
        fun ondelete(position: Int)
    }

}
