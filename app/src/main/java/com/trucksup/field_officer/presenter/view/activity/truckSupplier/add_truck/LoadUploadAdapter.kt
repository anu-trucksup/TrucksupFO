package com.trucksup.field_officer.presenter.view.activity.truckSupplier.add_truck

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadTyre

class LoadUploadAdapter(
    val context: Context,
    var list: List<AddLoadTyre>,
    var textTitle: String,
    var allPickerClick: LoadItemPikerClick
) : RecyclerView.Adapter<LoadUploadAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById<TextView>(R.id.title)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LoadUploadAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.all_picker_items, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: LoadUploadAdapter.ViewHolder, position: Int) {
        if (PreferenceManager.getLanguage(context!!) == "en") {
            if (textTitle == "Body type") {
                viewHolder.title.setText("" + list.get(position).name)
            } else {
                if (list.get(position).unit != null) {
                    viewHolder.title.setText("" + list.get(position).name + " " + list.get(position).unit)
                } else {
                    viewHolder.title.setText("" + list.get(position).name)
                }
            }
        } else {
            if (textTitle == "Body type") {
                viewHolder.title.setText(" " + list.get(position).hindiUnit)
            } else {
                if (list.get(position).unit != null) {
                    viewHolder.title.setText(list.get(position).name + " " + list.get(position).hindiUnit)
                } else {
                    viewHolder.title.setText(" " + list.get(position).name)
                }
            }
        }

        viewHolder.itemView.setOnClickListener {
            viewHolder.title.setText("" + list.get(position).hindiUnit)
            if (textTitle == "Body type") {
                allPickerClick.loadItomsClick(
                    textTitle,
                    "" + list.get(position).name,
                    "" + list.get(position).hindiUnit,
                    list.get(position).unit.toString(),
                    list.get(position).id!!
                )
            } else {
                var valueEn: String = list.get(position).name!!

                allPickerClick.loadItomsClick(
                    textTitle,
                    "" + list.get(position).name + " " + list.get(position).unit,
                    "" + list.get(position).name + " " + list.get(position).hindiUnit,
                    list.get(position).unit.toString(),
                    list.get(position).id!!
                )
            }

        }


    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {

        return list.size

    }
}