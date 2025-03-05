package com.trucksup.field_officer.presenter.common.parent

import androidx.fragment.app.DialogFragment
import com.trucksup.field_officer.presenter.common.dialog.ProgressDialog
import com.trucksup.field_officer.presenter.common.dialog.ProgressDialog.Companion.newInstance

class BaseDialogFragment : DialogFragment() {
    var dialog: ProgressDialog? = null

    fun showProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
        dialog = newInstance()
        dialog!!.show(parentFragmentManager)
        //        dialog.setCancelable(false);
    }

    fun dismissProgressDialog() {
        dialog!!.dismiss()
        dialog = null
    }
}
