package com.trucksup.field_officer.presenter.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class SharedPreference {

    fun isNextDateBetween(day: Int, month: Int, year: Int, nextDate:String, format:String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var dd:String=""+day
        if (day < 10) {
            dd= "0$day"
        }
        val myDate ="$year-$month-$dd"
        val curDate:String = getDateFormat("yyyy-MM-dd", format, nextDate)
        val dateMy = sdf.parse(myDate)
        val dateCut = sdf.parse(curDate)
        if(dateMy!! > dateCut) {
            println("Date 1 comes after Date 2");
            return true
        }
        else if(dateMy < dateCut) {
            println("Date 1 comes before Date 2");
            return false
        }
        else {
            return  true
        }
    }

    fun getDateFormat(outPutFormat:String, inputFormat:String, date:String):String {
        val convertedDate:String = formatDate(date, inputFormat, outPutFormat)!!;
        return convertedDate+""
    }

    private fun formatDate(dateToFormat: String, inputFormat: String?, outputFormat: String?): String? {
        try {
            val convertedDate = SimpleDateFormat(outputFormat).format(SimpleDateFormat(inputFormat).parse(dateToFormat)!!)
            return convertedDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun postLoadWindowDate(d:Int, dateFormat:String):String {
        val calendar: Calendar = GregorianCalendar()
        val sdf = SimpleDateFormat(dateFormat)
        calendar.add(Calendar.DATE, d)
        val day = sdf.format(calendar.time)
        Log.i("postLoadWindowDate", day)
        return  day
    }

    fun isNextDate(day: Int, month: Int, year: Int): Boolean {
        val calendar = Calendar.getInstance()
        val thisYear = calendar[Calendar.YEAR]
        Log.e("date", "# thisYear : $thisYear")
        val thisMonth = calendar[Calendar.MONTH]
        Log.e("date", "@ thisMonth : $thisMonth")
        val thisDay = calendar[Calendar.DAY_OF_MONTH]
        Log.e("date", "$ thisDay : $thisDay")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var dd:String=""+day
        if (day<10) {
            dd= "0$day"
        }
        val myDate ="$year-$month-$dd"
        val curDate:String=
            getDateFormat(
                "yyyy-MM-dd",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                getCurrentDate()
            )
        val dateMy = sdf.parse(myDate)
        val dateCut = sdf.parse(curDate)
        val strDate = sdf.parse(myDate)
        Log.e("date", "$ this date : $myDate")

        Log.e(" curant date", "$ this curant : ${
            getDateFormat(
                "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                getCurrentDate()
            )
        }")
        Log.e("date mim", "$ this date : "+System.currentTimeMillis()+" slect min "+strDate.time)

        if(dateMy!! > dateCut) {
            println("Date 1 comes after Date 2");
            return true
        }
        else if(dateMy < dateCut) {
            println("Date 1 comes before Date 2");
            return false
        }
        else {
            return  true
        }
    }

    fun getCurrentDate():String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val currentDate = sdf.format(Date())
        println(" C DATE is  $currentDate")
        return currentDate.toString()
    }

    private fun getPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("BusinessOfficer", Context.MODE_PRIVATE)
    }

    private fun getEdit(context: Context): SharedPreferences.Editor {
        return getPreference(context).edit()
    }

    fun saveUserId(context: Context, userId: String,securityToken: String?) {
        val edit = getEdit(context)
        edit.putString("USER_ID", userId)
        edit.putString("SECURITY_TOKEN",securityToken)
        edit.apply()
        edit.commit()
    }

    fun getUserId(context: Context): String? {
        val preferences = getPreference(context)
        return preferences.getString("USER_ID", "")
    }

    fun getSecurityToken(context: Context): String? {
        val preferences = getPreference(context)
        return preferences.getString("SECURITY_TOKEN", "")
    }

}