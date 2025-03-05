package com.trucksup.field_officer.presenter.common.btmsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.DeleteBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [DeleteBottomSheet.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DeleteBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private var mLogoutBinding: DeleteBottomSheetBinding? = null
    private var listener: OnDeleteProfileClick? = null

    private var msg: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mLogoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.delete_bottom_sheet, container, false)
        val view: View = mLogoutBinding!!.getRoot()
        initialize()
        return view
    }

    private fun initialize() {
        mLogoutBinding?.okButton?.setOnClickListener(this)
        mLogoutBinding?.cancelButton?.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_cancel, R.id.cancel_button -> dismiss()
            R.id.ok_button -> {
                dismiss()
                listener!!.ondeleteListener()
            }
        }
    }

    interface OnDeleteProfileClick {
        fun ondeleteListener()
    }

    companion object {
        var isEditCheck: Boolean? = null

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LogoutBottomSheet.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(
            clickListener: OnDeleteProfileClick?,
            msg: String?,
            isEdit: Boolean?
        ): DeleteBottomSheet {
            val dialog = DeleteBottomSheet()
            dialog.listener = clickListener
            dialog.msg = msg
            isEditCheck = isEdit
            return dialog
        }
    }
}