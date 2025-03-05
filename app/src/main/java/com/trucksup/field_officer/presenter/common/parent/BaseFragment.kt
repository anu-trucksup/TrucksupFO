package com.trucksup.field_officer.presenter.common.parent

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.fragment.app.Fragment
import com.trucksup.field_officer.presenter.common.dialog.ProgressDialog
import com.trucksup.field_officer.presenter.common.dialog.ProgressDialog.Companion.newInstance

class BaseFragment : Fragment() {
    var dialog: ProgressDialog? = null
    private var dialogTransparent: Dialog? = null

    fun showProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
        dialog = newInstance()
        //dialog.setCancelable(false);
        dialog!!.show(parentFragmentManager)
        //  dialog.setCancelable(false);
    }


    fun dismissProgressDialogCart() {
        if (dialogTransparent != null) {
            dialogTransparent!!.dismiss()
            dialogTransparent = null
        }
    }

    fun dismissProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }

        return false
    }
}
