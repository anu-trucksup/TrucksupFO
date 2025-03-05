package com.trucksup.field_officer.presenter.common.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.trucksup.field_officer.R

class ProgressDialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        setStyle(STYLE_NO_TITLE, R.style.Theme_FieldOfficer)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    fun show(manager: FragmentManager) {
        show(manager, dialogTag)
    }

    companion object {
        val dialogTag: String = ProgressDialog::class.java.simpleName

        @JvmStatic
        fun newInstance(): ProgressDialog {
            val fragment = ProgressDialog()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}
