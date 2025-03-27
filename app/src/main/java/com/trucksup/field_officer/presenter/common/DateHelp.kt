package com.trucksup.field_officer.presenter.common

import com.trucksup.field_officer.presenter.utils.LoggerMessage
import java.text.SimpleDateFormat

class DateHelp {

    fun countDayBetweenDate(startDate: String, endDate: String, formate: String): String {
        val mDateFormat = SimpleDateFormat(formate)

        // Converting the dates
        // from string to date format
        val mDate11 = mDateFormat.parse(startDate)
        val mDate22 = mDateFormat.parse(endDate)

        // Finding the absolute difference between
        // the two dates.time (in milli seconds)
        val mDifference = kotlin.math.abs(mDate11.time - mDate22.time)

        // Converting milli seconds to dates
        val mDifferenceDates = mDifference / (24 * 60 * 60 * 1000)

        // Converting the above integer to string
        val mDayDifference = mDifferenceDates.toString()

        // Displaying the result in the TextView
        LoggerMessage.logErrorMsg(
            "Difference Date",
            "The difference between two dates is $mDayDifference days"
        )
        return mDayDifference
    }
}