package com.trucksup.field_officer.presenter.view.interfaces

import android.app.AlertDialog
import com.trucksup.field_officer.databinding.CityDialogBinding

interface SearchLocation {

    fun searchLocation(dialog: AlertDialog,binding: CityDialogBinding)

}