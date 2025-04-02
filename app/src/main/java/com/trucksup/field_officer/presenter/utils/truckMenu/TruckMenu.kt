package com.trucksup.field_officer.presenter.utils.truckMenu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.logistics.trucksup.truckMenu.TruckListCantroler
import com.trucksup.field_officer.R


object TruckMenu {
    private var mypopupWindow: PopupWindow? = null


    @RequiresApi(Build.VERSION_CODES.M)
    fun OptionMenu(context: Activity, v: View?, menu: TruckListCantroler) {


        val add: TextView
        val remove: TextView

        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.truck_menu, null)
        add = view.findViewById(R.id.add)
        remove = view.findViewById(R.id.remove)


        add?.setOnClickListener {
            menu.addTruck()
            mypopupWindow?.dismiss()
        }

        remove?.setOnClickListener {
            menu.removeTruck()
            mypopupWindow?.dismiss()
        }

        mypopupWindow = PopupWindow(
            view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        mypopupWindow!!.setOutsideTouchable(true)
        mypopupWindow!!.setFocusable(true)
        mypopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mypopupWindow!!.setElevation(20F)
        }
        show(v)
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.M)
    fun aboutPlan(context: Activity, v: View?, name: String, textPlan: String) {


        val planName: TextView

        val text: TextView

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.about_plan_box, null)
        planName = view.findViewById<TextView>(R.id.planName)
        text = view.findViewById(R.id.text)

        text.text = textPlan.toString()

        planName.text = name.toString()

        if (TextUtils.isEmpty(name)) {
            planName?.visibility = View.GONE
        }

        mypopupWindow = PopupWindow(
            view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        mypopupWindow!!.isOutsideTouchable = true
        mypopupWindow!!.isFocusable = true
        mypopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mypopupWindow!!.elevation = 20F
        }
        show(v)
    }

    fun show(v: View?) {
        mypopupWindow?.showAsDropDown(v, -153, 0)
    }

}