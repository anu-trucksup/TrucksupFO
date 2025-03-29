package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.chip.Chip
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager

class LoanChipAdapter(
    private val context: Context,
    private val chipTextList: ArrayList<chipData>,
    var cantroler: ChipController
) : BaseAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = chipTextList.size

    override fun getItem(position: Int): Any = chipTextList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View =
            convertView ?: layoutInflater.inflate(R.layout.loan_amount_chip, parent, false)

        val chip = view.findViewById<Chip>(R.id.chip)
        chip.typeface = Typeface.create(
            ResourcesCompat.getFont(context, R.font.bai_jamjuree_semi_bold),
            Typeface.NORMAL
        )

        val textColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),  // Checked state (selected)
                intArrayOf(-android.R.attr.state_checked)  // Unchecked state (not selected)
            ),
            intArrayOf(
                ContextCompat.getColor(context, R.color.blue),  // Color when selected
                ContextCompat.getColor(context, R.color.primery_text) // Color when not selected
            )
        )

// Apply the ColorStateList to the chip text color
        chip.setTextColor(textColorStateList)

        if (PreferenceManager.getLanguage(context) == "en") {
            if (chipTextList[position].optionName != null) {
                chip.text = chipTextList[position].optionName
            } else {
                chip.text = ""
            }
        } else {
            if (chipTextList[position].optionNameHindi != null) {
                chip.text = chipTextList[position].optionNameHindi
            } else {
                chip.text = ""
            }
        }
        if (chipTextList[position].isClick == true) {
            chip.isChecked = true
            chip.isCheckedIconVisible = true
        } else {
            chip.isChecked = false
            chip.isCheckedIconVisible = false
        }



        chip.setOnClickListener {

            if (chipTextList[position].isClick == true) {
                chipTextList.set(
                    position,
                    chipData(
                        chipTextList[position].optionName,
                        chipTextList[position].optionNameHindi,
                        false
                    )
                )
            } else {
                chipTextList.set(
                    position,
                    chipData(
                        chipTextList[position].optionName,
                        chipTextList[position].optionNameHindi,
                        true
                    )
                )

            }
            LoggerMessage.LogErrorMsg("Click on Chip", "Click on Chip ==== true")

            var data = ArrayList<chipData>()


            for (i in 0 until chipTextList.size) {
                if (i == position) {
                    data.add(
                        chipData(
                            chipTextList[i].optionName,
                            chipTextList[i].optionNameHindi,
                            true
                        )
                    )
                } else {
                    data.add(
                        chipData(
                            chipTextList[i].optionName,
                            chipTextList[i].optionNameHindi,
                            false
                        )
                    )
                }
            }

            notifyDataSetChanged()
            if (PreferenceManager.getLanguage(context).toString() == "en") {
                cantroler.updateData(data, chipTextList.get(position).optionName.toString())
            } else {
                cantroler.updateData(data, chipTextList.get(position).optionNameHindi.toString())
            }

        }

        return view
    }
}