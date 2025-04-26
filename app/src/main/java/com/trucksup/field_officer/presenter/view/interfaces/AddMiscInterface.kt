package com.trucksup.field_officer.presenter.view.interfaces

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.trucksup.field_officer.databinding.AddMiscLayoutBinding
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetAllMiscLeadResponse


interface AddMiscInterface {

    fun addMisLayout(v: AddMiscLayoutBinding,dialog: BottomSheetDialog,data: GetAllMiscLeadResponse.IncompletedLead?)

}