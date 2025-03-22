package com.trucksup.field_officer.presenter.common.dialog

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.trucksup.field_officer.R

class ProgressDialogBox(context: Context) : Dialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.progress_box)
        this.getWindow()?.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        this.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false)

    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        this.dismiss()
        super.setOnDismissListener(listener)
    }
}