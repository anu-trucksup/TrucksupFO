package com.trucksup.field_officer.presenter.common

import android.content.Context

class LoadingUtils {

    companion object {
        private var jarvisLoader: ProgressDailogBox? = null
        fun showDialog(
            context: Context?,
            isCancelable: Boolean
        ) {
            hideDialog()
            if (context != null) {
                try {
                    jarvisLoader = ProgressDailogBox(context)
                    jarvisLoader?.let { jarvisLoader ->
                        jarvisLoader.setCanceledOnTouchOutside(true)
                        jarvisLoader.setCancelable(isCancelable)
                        jarvisLoader.show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun hideDialog() {
            if (jarvisLoader != null && jarvisLoader?.isShowing!!) {
                jarvisLoader?.dismiss()
            }
        }

    }

}