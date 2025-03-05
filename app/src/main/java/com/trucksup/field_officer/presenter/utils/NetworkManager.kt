package com.trucksup.field_officer.presenter.utils

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object NetworkManager {

    fun isConnect(context: Context):Boolean
    {
        var connectivity : ConnectivityManager? = context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        var info : NetworkInfo? = null
        var isCon:Boolean=true
        if ( connectivity != null)
        {
            info = connectivity!!.activeNetworkInfo

            if (info != null)
            {
                if (info!!.state == NetworkInfo.State.CONNECTED)
                {
                    isCon=  true
                    // Toast.makeText(context, "CONNECTED", Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                isCon=  false
                // Toast.makeText(context, "NOT CONNECTED", Toast.LENGTH_LONG).show()
            }
        }
        return isCon;
    }

}