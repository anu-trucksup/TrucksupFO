package com.trucksup.field_officer.presenter.view.interfaces

import android.app.AlertDialog
import com.trucksup.field_officer.databinding.AddLeadLayoutBinding

interface AddLeadInterface {
    fun onLocation(dialog: AlertDialog,binding: AddLeadLayoutBinding)
}