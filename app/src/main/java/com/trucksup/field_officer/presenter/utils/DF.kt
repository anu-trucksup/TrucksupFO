package com.trucksup.field_officer.presenter.utils

import android.widget.EditText
import java.util.regex.Matcher
import java.util.regex.Pattern

class DF(
    var AcountHolderName: EditText,
    var ETAccountHolderNumber: EditText,
    var ETReenterAccountNumber: EditText,
    var ETBankNameNOB: EditText,
    var ETIfscCodeNOB: EditText,
    var ETPanNumberNOB: EditText
) {


    fun CheckValidation() {
        if (AcountHolderName.text.isEmpty()) {
            this.AcountHolderName.requestFocus()
            this.AcountHolderName.setError("Hlo")
        } else if (ETAccountHolderNumber.text.isEmpty()) {
            ETAccountHolderNumber.requestFocus()
            ETAccountHolderNumber.setError("")
        } else if (ETReenterAccountNumber.text.isEmpty()) {
            ETReenterAccountNumber.requestFocus()
            ETReenterAccountNumber.setError("")
        } else if (ETBankNameNOB.text.isEmpty()) {
            ETBankNameNOB.requestFocus()
            ETBankNameNOB.setError("")
        } else if (ETIfscCodeNOB.text.isEmpty()) {
            ETIfscCodeNOB.requestFocus()
            ETIfscCodeNOB.setError("")
        } else if (ETPanNumberNOB.length() == 10) {
            val s = ETPanNumberNOB.toString() // get your editext value here
            val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
            val matcher: Matcher = pattern.matcher(s)
            // Check if pattern matches
            if (matcher.matches()) {
                //  panNumber = ETPanNumberNOB.toString()
            } else {
                //LoggerMessage.tostPrint("Enter Right PAN No", this)
                ETPanNumberNOB?.setError("Enter Right PAN Noss")
                // pan_no?.setText("")
            }
        } else {
            ETPanNumberNOB.requestFocus()
            /*val customErrorDrawable = resources.getDrawable(com.trucksup.fieldofficer.R.drawable.ic_phone)
            customErrorDrawable.setBounds(
                0,
                0,
                customErrorDrawable.intrinsicWidth,
                customErrorDrawable.intrinsicHeight
            )
            ETPanNumberNOB?.setError("Enter Right PAN No" , customErrorDrawable)*/
            ETPanNumberNOB?.setError("Enter Right PAN No")
            ETPanNumberNOB?.setText("")
        }

    }
}