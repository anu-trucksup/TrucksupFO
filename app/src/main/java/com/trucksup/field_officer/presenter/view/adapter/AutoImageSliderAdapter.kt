package com.trucksup.field_officer.presenter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.trucksup.field_officer.R

class AutoImageSliderAdapter(private val context: Context, private var imageList: List<String>) : PagerAdapter() {

    override fun getCount() =imageList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =  (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater).inflate(R.layout.image_slider_item, null)
        val ivImages = view.findViewById<ImageView>(R.id.imageView)

        //ivImages.setImageResource(imageList[position])

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }
}