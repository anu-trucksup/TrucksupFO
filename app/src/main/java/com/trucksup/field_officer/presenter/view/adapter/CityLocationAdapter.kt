package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.cityPicker.CityClick
import com.trucksup.field_officer.presenter.cityPicker.CitySearchData
import com.trucksup.field_officer.presenter.utils.PreferenceManager

class CityLocationAdapter(
    val context: Context,
    var clickCity: CityClick,
    var type: String,
    var city: List<CitySearchData>
) : RecyclerView.Adapter<CityLocationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val image: ImageView = itemView.findViewById<ImageView>(R.id.image)
        val city_name: TextView = itemView.findViewById<TextView>(R.id.city_name)
        val state_name: TextView = itemView.findViewById<TextView>(R.id.state_name)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityLocationAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.city_location_items, parent, false)

        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: CityLocationAdapter.ViewHolder, position: Int) {

        if (PreferenceManager.getLanguage(context).toString() == "en") {
            viewHolder.city_name.setText(city.get(position).city)
            viewHolder.state_name.setText(city.get(position).state)
        } else {
            viewHolder.city_name.setText(city[position].hindiCityName)
            viewHolder.state_name.setText(city[position].hindiStateName)
        }



        viewHolder.itemView.setOnClickListener {


            if (city.get(position).state.contains(',')) {
                var st: String =
                    city.get(position).state.substring(0, city.get(position).state.indexOf(','))
                clickCity.onClickcity(city.get(position).city, st, "")
            } else {
                clickCity.onClickcity(
                    city.get(position).city,
                    city.get(position).state,
                    city.get(position).city
                )
                clickCity.onClickcityLanguage(
                    city.get(position).city,
                    city.get(position).hindiCityName,
                    city.get(position).state,
                    city.get(position).hindiStateName,
                    ""
                )
            }
            // clickCity.onClickcityLanguage(city.get(position).City,city.get(position).City,city.get(position).State,city.get(position).State,city.get(position).CityId)

        }

    }

    //    fun setFilter( fi_city: List<CityNameData>)
//    {
//        city=fi_city;
//        notifyDataSetChanged()
//    }
    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return city.size
    }
}