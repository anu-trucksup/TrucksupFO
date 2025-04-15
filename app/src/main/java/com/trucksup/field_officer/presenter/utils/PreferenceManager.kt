package com.trucksup.field_officer.presenter.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Math.abs
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object PreferenceManager {

    var FIRSTTIME: String = "FIRSTTIME"
    var TABLEFIRST: String = "table_trucksup_firstlogin"
    var LANGUAGE: String = "language"
    var REFRALCODE: String = "REFRALCODE"
    var PROFILE_STATES: String = "states"
    var ROAL: String = "roal"
    var PROFILETYPE: String = "PROFILETYPE"
    var PHONENO: String = "phone"
    var VEHICLENO: String = "vehicleNo"
    var NOOFWHEELS: String = "noOfWheels"
    var PATH: String = "paths"
    var STRINGTIME: String = "stringtime"
    var TABLE: String = "table_trucksup"
    var TABLE_LAND: String = "table_trucksup_lang"
    var TOKEN: String = "token"
    var FASTIVEL: String = "fastivel"
    var CongratulationsHome: String = "CongratulationsHome"
    var LOGOUT: String = "logout"
    var USER_DATA: String = "data"
    var SUBDATA: String = "SUBDATA"
    var VisitingCard: String = "visiting"
    var PRODECTTYPE: String = "PRODECTTYPE"
    var NOTIFICATIONCOUNT: String = "NOTIFICATIONCOUNT"
    var USERNAME: String = "name"
    var PROFILEID: String = "1"
    var USERIMG: String = "image"
    var BrokerVerify: String = "isBrokerVerify"
    var PROFILELOCKED: String = "PROFILELOCKED"
    var PROFILEVERIFY: String = "PROFILEVERIFY"
    var POSTLOCK: String = "POSTLOCK"
    var WINDOWDATE: String = "WINDOWDATE"
    var GRACETIME: String = "GRACETIME"
    var VERYWINDOWDATE: String = "VERYWINDOWDATE"
    var KYCWINDOW: String = "KYCWINDOW"
    var FCM: String = "FCM"
    var KYC: String = "KYC"
    var accessHeader: String = "header"
    var accessPassword: String = "accessPassword"
    var accessKey: String = "accessKey"
    var BACKGROUNDAPP: String = "backGroundApp"
    var accessUserName: String = "accessUserName"
    var accessUserAgaint: String = "accessUserAgaint"
    var accessUserIssuer: String = "accessUserIssuer"
    var ALART_DATE: String = "alartdate"
    var BODYTYPE: String = "bodyType"
    var OTP: String = "OTP"
    var SERVERURL: String = "serverurl"
    var MENULIST: String = "menulistNew"
    var MSGLINE: String = "msg"
    var MSGLINEHI: String = "msghi"
    var AUTHTOKENOTP: String = "authtokenotp"
    var TRUCKSHUBAUTHTOKE: String = "truckshubauthtoke"
    var AUTHTOKENOTPNEW: String = "authtokenotpNEW"
    var KYCDATE: String = "kycdate"
    var TRUCKCOUNT: String = "truckcount"
    var PEREFRREDLAN: String = "PEREFRREDLAN"

    fun isFirstTime(myContext: Context): Boolean {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLEFIRST, MODE_PRIVATE)
        return sharedPreferences.getBoolean(FIRSTTIME, true)
    }


    fun setFirstTime(phone: Boolean, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLEFIRST, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(FIRSTTIME, phone)

        editor.apply()
        editor.commit()
    }

    fun isKyc(myContext: Context): Boolean {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLEFIRST, MODE_PRIVATE)
        return sharedPreferences.getBoolean(KYC, true)!!
    }


    fun setKyc(kyc: Boolean, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLEFIRST, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KYC, kyc)

        editor.apply()
        editor.commit()
    }

    fun setApiPath(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PATH, path)
        editor.apply()
        editor.commit()
    }

    fun getApiPath(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(PATH, "")!!
    }


    fun setCongrtulationHome(path: Boolean, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(CongratulationsHome, path)
        editor.apply()
        editor.commit()
    }

    fun getCongrtulationHome(myContext: Context): Boolean {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getBoolean(CongratulationsHome, true)!!
    }


    fun setPereferredLanCount(path: Int, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(PEREFRREDLAN, path)
        editor.apply()
        editor.commit()
    }

    fun getPereferredLanTruckCount(myContext: Context): Int {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getInt(PEREFRREDLAN, 0)!!
    }

    fun setAccesUserInssur(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(accessUserIssuer, path)
        editor.apply()
        editor.commit()
    }

    fun getAccesUserInssur(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(accessUserIssuer, "")!!
    }

    fun setAccesUserName(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(accessUserName, path)
        editor.apply()
        editor.commit()
    }

    fun getAccesUserName(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(accessUserName, "")!!
    }

    fun setAccesKey(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(accessKey, path)
        editor.apply()
        editor.commit()
    }

    fun getAccessKey(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(accessKey, "")!!
    }

    fun setBackGroundApp(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(BACKGROUNDAPP, path)
        editor.apply()
        editor.commit()
    }

    fun getBackGroundApp(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(BACKGROUNDAPP, "")!!
    }

    fun setAccesPassword(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(accessPassword, path)
        editor.apply()
        editor.commit()
    }

    fun getAccesPassword(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(accessPassword, "")!!
    }

    fun setAccesUserAgaint(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(accessUserAgaint, path)
        editor.apply()
        editor.commit()
    }

    fun getAccesUserAgaint(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(accessUserAgaint, "")!!
    }

    fun setAccesHeader(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(accessHeader, path)
        editor.apply()
        editor.commit()
    }

    fun getAccesHeader(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(accessHeader, "")!!
    }

    fun setTruckCount(path: Int, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(TRUCKCOUNT, path)
        editor.apply()
        editor.commit()
    }

    fun getTruckCount(myContext: Context): Int {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getInt(TRUCKCOUNT, 0)!!
    }

    fun setMenuList(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(MENULIST, path)
        editor.apply()
        editor.commit()
    }

    fun getMenuList(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(MENULIST, "")!!
    }

    fun setSubData(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(SUBDATA, path)
        editor.apply()
        editor.commit()
    }


    fun setAuthOtp(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(AUTHTOKENOTP, path)
        editor.apply()
        editor.commit()
    }

    fun getAuthOtpNew(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(AUTHTOKENOTPNEW, "")!!
    }


    fun setAuthOtpNew(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(AUTHTOKENOTPNEW, path)
        editor.apply()
        editor.commit()
    }

    fun getAuthOtp(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(AUTHTOKENOTP, "")!!
    }

    fun setTrucksHubAith(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(TRUCKSHUBAUTHTOKE, path)
        editor.apply()
        editor.commit()
    }

    fun getTrucksHubAith(myContext: Context): String {


        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(TRUCKSHUBAUTHTOKE, "")!!
    }


    fun setFastivel(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(FASTIVEL, path)
        editor.apply()
        editor.commit()
    }

    fun getFastivel(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(FASTIVEL, "")!!
    }


    fun setOtp(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(OTP, path)
        editor.apply()
        editor.commit()
    }

    fun getOtp(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(OTP, "")!!
    }

    fun setKycDate(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KYCDATE, path)
        editor.apply()
        editor.commit()
    }

    fun getKycDate(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(KYCDATE, "")!!
    }

    fun setMsgLineEn(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(MSGLINE, path)
        editor.apply()
        editor.commit()
    }

    fun getMsgLineEn(myContext: Context): String {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(MSGLINE, "")!!
    }


    fun setServerUrl(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(SERVERURL, path)
        editor.apply()
        editor.commit()

    }

    fun getServerUrl(myContext: Context): String {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(SERVERURL, "")!!
    }

    fun setProfileStates(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(PROFILE_STATES, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PATH, path)
        editor.apply()
        editor.commit()

    }

    fun getProfileStates(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(PROFILE_STATES, MODE_PRIVATE)
        return sharedPreferences.getString(PATH, "")!!
    }


    fun setReferralCode(path: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(REFRALCODE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PATH, path)
        editor.apply()
        editor.commit()
    }

    fun getReferralCode(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(REFRALCODE, MODE_PRIVATE)
        return sharedPreferences.getString(PATH, "")!!
    }

    /*  @SuppressLint("SuspiciousIndentation")
      fun stringToPath(pathString: String): PathsModle {
          val gson = Gson()
          var pathData: PathsModle = gson.fromJson(pathString, PathsModle::class.java)
          return pathData
      }

      fun stringToAuth(pathString: String): AuthTokenData {
          val gson = Gson()
          var pathData: AuthTokenData = gson.fromJson(pathString, AuthTokenData::class.java)
          return pathData
      }

      fun trucksHubAuthData(context: Context): TrucksHubAuthData {
          var pathString: String = getTrucksHubAith(context)
          val gson = Gson()
          var pathData: TrucksHubAuthData = gson.fromJson(pathString, TrucksHubAuthData::class.java)
          return pathData
      }

      fun stringToAuthNew(pathString: String): OtpAuthData {
          val gson = Gson()
          var pathData: OtpAuthData = gson.fromJson(pathString, OtpAuthData::class.java)
          return pathData
      }*/

    fun setFCM(token: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(FCM, token)
        editor.apply()
        editor.commit()
    }

    fun getNotificationCount(myContext: Context): Int {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getInt(NOTIFICATIONCOUNT, 0)!!
    }

    fun setNotificationCount(value: Int, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(NOTIFICATIONCOUNT, value)
        editor.apply()
        editor.commit()
    }

    fun getFCM(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(FCM, "")!!
    }

    fun setToken(phone: String, myContext: Context) {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(TOKEN, phone)
        editor.apply()
        editor.commit()
    }

    fun getUserName(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(USERNAME, "")!!
    }

    fun setWindowDAte(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(WINDOWDATE, data)
        editor.apply()
        editor.commit()
    }

    fun getVeryWindowDAte(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(VERYWINDOWDATE, "n")!!
    }


    fun setKycWindowDAte(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KYCWINDOW, data)
        editor.apply()
        editor.commit()
    }

    fun getKycVeryWindowDAte(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(KYCWINDOW, "n")!!
    }


    fun setVeryWindowDAte(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(VERYWINDOWDATE, data)
        editor.apply()
        editor.commit()
    }

    fun getGraceTime(myContext: Context): Int {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getInt(GRACETIME, 0)!!
    }

    fun setGraceTime(data: Int, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(GRACETIME, data)
        editor.apply()
        editor.commit()
    }

    fun getWindowDAte(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(WINDOWDATE, "n")!!
    }

    fun setUserName(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(USERNAME, data)
        editor.apply()
        editor.commit()
    }

    fun setProfileId(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PROFILEID, data)
        editor.apply()
        editor.commit()
    }

    fun getProfileId(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(PROFILEID, "")!!
    }

    fun setAlartWindowDAte(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(ALART_DATE, data)
        editor.apply()
        editor.commit()
    }

    fun getAlartWindowDAte(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(ALART_DATE, "n")!!
    }

    fun getBrokerIsVerified(myContext: Context): Boolean {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getBoolean(BrokerVerify, false)!!
    }

    fun setBrokerIsVerified(data: Boolean, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(BrokerVerify, data)
        editor.apply()
        editor.commit()
    }

    fun getLockedProfile(myContext: Context): Boolean {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getBoolean(PROFILELOCKED, false)!!
    }

    fun setLockedProfile(data: Boolean, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(PROFILELOCKED, data)
        editor.apply()
        editor.commit()
    }

    fun getUserImage(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(USERIMG, "")!!
    }

    fun setUserImage(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(USERIMG, data)
        editor.apply()
        editor.commit()
    }


    fun getProfileVerify(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(PROFILEVERIFY, "")!!
    }

    fun setProfileVerify(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PROFILEVERIFY, data)
        editor.apply()
        editor.commit()
    }

    fun getPostLock(myContext: Context): String {

        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(POSTLOCK, "")!!
    }

    fun setPostLock(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(POSTLOCK, data)
        editor.apply()
        editor.commit()
    }


    fun isLogout(myContext: Context): Boolean {
        // LoggerMessage.LogErrorMsg("Start", "is logout")
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getBoolean(LOGOUT, false)
    }


    fun setLogout(phone: Boolean, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(LOGOUT, phone)

        editor.apply()
        editor.commit()
    }

    fun getToken(myContext: Context): String {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN, "")!!
    }


    fun setPhoneNo(phone: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PHONENO, phone)
        editor.apply()
        editor.commit()
    }

    /*fun getUserData(myContext: Context): ProfileDetails? {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, Context.MODE_PRIVATE)
        var profile: ProfileDetails? = null
        var jsonString: String = sharedPreferences.getString(USER_DATA, "")!!
        if (!TextUtils.isEmpty(jsonString)) {
            val gson = Gson()
            profile = gson.fromJson(jsonString, ProfileDetails::class.java)
        }
        if (profile != null) {
            return profile!!
        } else {
            return null
        }

    }

    fun getVisitingCardData(myContext: Context): VisitingCardData? {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, Context.MODE_PRIVATE)
        var profile: VisitingCardData? = null
        var jsonString: String = sharedPreferences.getString(VisitingCard, "")!!
        if (!TextUtils.isEmpty(jsonString)) {
            val gson = Gson()
            profile = gson.fromJson(jsonString, VisitingCardData::class.java)
        }
        if (profile != null) {
            return profile!!
        } else {
            return null
        }

    }*/

    fun setVisitingCardData(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(VisitingCard, data)
        editor.apply()
        editor.commit()
    }

    fun setUserData(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(USER_DATA, data)
        editor.apply()
        editor.commit()
    }

    fun setVehicleNo(myContext: Context, vehicleNo: String) {
        val preferences: SharedPreferences = myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(VEHICLENO, vehicleNo) // Replace "key_name" with a unique key for your data
        editor.apply()
    }

    fun getVehicleNo(myContext: Context): String? {
        val preferences: SharedPreferences = myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return preferences.getString(VEHICLENO, null)

    }

    fun setNoOfWheels(myContext: Context, noOfWheels: String) {
        val preferences: SharedPreferences = myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(
            NOOFWHEELS,
            noOfWheels
        ) // Replace "key_name" with a unique key for your data
        editor.apply()
    }

    fun getNoOfWheels(myContext: Context): String? {
        val preferences: SharedPreferences = myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return preferences.getString(NOOFWHEELS, null)

    }


    fun setProdectType(data: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PRODECTTYPE, data)
        editor.apply()
        editor.commit()
    }

    fun getPhoneNo(myContext: Context): String {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(PHONENO, "")!!
    }

    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  " + currentDate)
        return currentDate.toString()
    }


    fun getCurantDateTime(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  " + currentDate)
        return currentDate.toString()
    }

    @SuppressLint("NewApi")
    fun getTokenDate(time: String): Date {
        var date: Date? = null
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSXXX")
        val dateFormat2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        try {
            date = dateFormat.parse(time)
//             out = dateFormat2.format(date)
//            Log.e("Time", out)
        } catch (e: ParseException) {
        }
        return date!!
    }

    fun getBackDate(time: Int): String {
        val dateFormat: DateFormat = SimpleDateFormat("MM-dd-yyyy")
        val cal = Calendar.getInstance()
        val calReturn = Calendar.getInstance()
        calReturn.add(Calendar.DATE, time)
        var dt: String = dateFormat.format(calReturn.time)
        return dt
    }

    fun setTokenTime(phone: String, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(STRINGTIME, phone)
        editor.apply()
        editor.commit()
    }

    fun getTokenTime(myContext: Context): String {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(STRINGTIME, "")!!
    }

    fun getRequestNo(): String {
        val random = Random()
        val otp = String.format("%08d", random.nextInt(10000000))
        return otp
    }


    fun getProfileType(myContext: Context): Int {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getInt(PROFILETYPE, 1)!!
    }

    fun setProfileType(roal: Int, myContext: Context) {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(PROFILETYPE, roal)
        editor.apply()
        editor.commit()
    }

    fun getRoal(myContext: Context): String {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE, MODE_PRIVATE)
        return sharedPreferences.getString(ROAL, "")!!
    }

    fun getLanguage(myContext: Context): String {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences(TABLE_LAND, MODE_PRIVATE)
        return sharedPreferences.getString(LANGUAGE, "en")!!
    }

    fun removeData(context: Context) {

        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(TABLE, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun isTokenTime(context: Context): Boolean {
        var boolean: Boolean = false

        var currentDate: Date = Date()
        if (getTokenDate(getTokenTime(context))?.after(currentDate)!!) {
            return true
        } else {
            return false
        }
    }

    @SuppressLint("NewApi", "SuspiciousIndentation")
    fun getAuthToken(): String {

        val authPayload = "BOAPISERVICES:BOAPISERVICES@2025"
        val data = authPayload.toByteArray()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val encoder: Base64.Encoder = Base64.getEncoder()
            val encoded: String = encoder.encodeToString(data)

            return "Basic " + encoded
        } else {
            val encode = android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT);
            return "Basic " + encode
        }

    }

    @SuppressLint("NewApi")
    fun getHelpAuthToken(): String {
        val authPayload = "R3xZbtKM84QyaoATxV5d:X"
        val data = authPayload.toByteArray()
        val encoder: Base64.Encoder = Base64.getEncoder()
        val encoded: String = encoder.encodeToString(data)
        return "Basic " + encoded
    }

    @SuppressLint("NewApi")
    fun encodeImage(activity: Activity, imageUri: Uri): String {
        val input = activity.getContentResolver().openInputStream(imageUri)
        val image = BitmapFactory.decodeStream(input, null, null)

        // Encode image to base64 string
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()
        val encoder: Base64.Encoder = Base64.getEncoder()
        val imageString: String = encoder.encodeToString(imageBytes)
        //val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return imageString
    }


    fun prepareFilePartTrucksHum(uploadImageFilefront: File, name: String): MultipartBody.Part {

        // use the FileUtils to get the actual file by uri
        val file: File = uploadImageFilefront

        // create RequestBody instance from file
        val requestFile: RequestBody = RequestBody.create(
            "file/*".toMediaTypeOrNull(),
            file
        )

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(name, file.getName(), requestFile)
    }

    fun getDateFormet(OutPutFormat: String, inputFormat: String, date: String): String {
        val convertedDate: String = formatDate(date, inputFormat, OutPutFormat)!!;
        return convertedDate + ""
    }

    fun getCurantTime(): String {
        var calander = Calendar.getInstance();
        var simpleDateFormat = SimpleDateFormat("hh:mm a");

        var time = simpleDateFormat.format(calander.getTime());
        return time
    }

    fun formatDate(dateToFormat: String, inputFormat: String?, outputFormat: String?): String? {

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
//            val parsedDate = LocalDateTime.parse(dateToFormat, DateTimeFormatter.ISO_DATE_TIME)
//            val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern(outputFormat))
//            return formattedDate
//        } else{
//            val parser =  SimpleDateFormat(inputFormat)
//            val formatter = SimpleDateFormat(outputFormat)
//            val formattedDate = formatter.format(parser.parse(dateToFormat))
//            return formattedDate
//        }

        try {
            // Logger.e("DATE", "Input Date Date is $dateToFormat")
            val convertedDate = SimpleDateFormat(outputFormat).format(
                SimpleDateFormat(inputFormat).parse(dateToFormat)
            )
            // Logger.e("DATE", "Output Date is $convertedDate")

            //Update Date
            return convertedDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }


    /* fun isBlock(context: Context): Boolean {
         if (PreferenceManager.getUserData(context) == null) {

             // LoggerMessage.tostPrint("Broker brofile null",context)
             var abx: IntentDailogbox =
                 IntentDailogbox(context, context.resources.getString(R.string.com_broker), "i")
             abx?.show()
             return false

         }
         if (PreferenceManager.getUserData(context)?.brokerStatus != null && PreferenceManager.getUserData(
                 context
             )?.brokerStatus!!.contains("BlackList")
         ) {

             return true
         }

         return false
     }

     fun postLoadBrokerCheck(context: Context): Boolean {

         if (PreferenceManager.getUserData(context) == null) {

             // LoggerMessage.tostPrint("Broker brofile null",context)
             var abx: IntentDailogbox =
                 IntentDailogbox(context, context.resources.getString(R.string.com_broker), "i")
             abx?.show()
             return false

         } else if (PreferenceManager.getUserData(context)?.firmName == null || PreferenceManager.getUserData(
                 context
             )?.firmName == ""
         ) {

             //LoggerMessage.tostPrint("Broker brofile null",context)
             var abx: IntentDailogbox =
                 IntentDailogbox(context, context.resources.getString(R.string.com_broker), "i")
             abx?.show()
             return false
         } else if (PreferenceManager.getUserData(context)?.isKycUploaded == false) {
             var abx: IntentDailogbox =
                 IntentDailogbox(context, context.resources.getString(R.string.com_kyc), "k")
             abx?.show()
             return false
         } else if (PreferenceManager.getUserData(context)?.isBrokerVerify == "N" && getVeryWindowDAte(
                 context
             ).toString() == "n"
         ) {

             Log.e("Very fy date", ">>>>>>>" + getVeryWindowDAte(context))
             var abx: ProfileRequestBox = ProfileRequestBox(
                 context,
                 context.resources.getString(R.string.com_isBrokerVerifiy),
                 "BrokerVerification"
             )
             abx?.show()
             return false


         } else if (PreferenceManager.getUserData(context)?.isBrokerVerify == "N" && PreferenceManager.isNextDate(
                 PreferenceManager.getDateFormet(
                     "dd", "dd-MM-yyyy HH:mm a",
                     getVeryWindowDAte(context) + ""
                 ).toString().toInt(),
                 PreferenceManager.getDateFormet(
                     "MM", "dd-MM-yyyy HH:mm a",
                     getVeryWindowDAte(context) + ""
                 ).toString().toInt(),
                 PreferenceManager.getDateFormet(
                     "yyyy",
                     "dd-MM-yyyy HH:mm a",
                     getVeryWindowDAte(context) + ""
                 ).toString().toInt()
             ) == false
         ) {

             Log.e("Very fy date", ">>>>>>>" + getVeryWindowDAte(context))
             var abx: MyAlartBox =
                 MyAlartBox(
                     context as Activity,
                     context.resources.getString(R.string.req_message),
                     "m"
                 )
             abx?.show()
             return false
         } else if (PreferenceManager.getUserData(context)?.loadLocked == null || PreferenceManager.getUserData(
                 context
             )?.loadLocked.toString()
                 .lowercase() == "y" && PreferenceManager.getPostLock(context) == ""
         ) {

             var abx: ProfileRequestBox = ProfileRequestBox(
                 context,
                 context.resources.getString(R.string.profile_isLock),
                 "ProfileLocked"
             )
             abx?.show()
             return false

         } else if (PreferenceManager.getUserData(context)?.loadLocked == null || PreferenceManager.getUserData(
                 context
             )?.loadLocked.toString().lowercase() == "y" && PreferenceManager.isNextDate(
                 PreferenceManager.getDateFormet(
                     "dd", "dd-MM-yyyy HH:mm a",
                     getWindowDAte(context) + ""
                 ).toString().toInt(),
                 PreferenceManager.getDateFormet(
                     "MM", "dd-MM-yyyy HH:mm a",
                     getWindowDAte(context) + ""
                 ).toString().toInt(),
                 PreferenceManager.getDateFormet(
                     "yyyy",
                     "dd-MM-yyyy HH:mm a",
                     getWindowDAte(context) + ""
                 ).toString().toInt()
             ) == false
         ) {
             var abx: MyAlartBox = MyAlartBox(
                 context as Activity,
                 context.resources.getString(R.string.req_message),
                 "m"
             )
             abx?.show()
             return false
         } else {
             return true
             // context.startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" +loadList.get(position).VNumber)));
         }
     }

     fun BrokerVerified(context: Context): Boolean {

         LoggerMessage.LogErrorMsg(
             "KYC upload",
             PreferenceManager.getUserData(context)?.isKycUploaded.toString()
         )
         if (PreferenceManager.getUserData(context) == null) {

             // LoggerMessage.tostPrint("Broker brofile null",context)
             var abx: IntentDailogbox =
                 IntentDailogbox(context, context.resources.getString(R.string.com_broker), "i")
             abx?.show()
             return false

         } else if (PreferenceManager.getUserData(context)?.firmName == null || PreferenceManager.getUserData(
                 context
             )?.firmName == ""
         ) {

             //LoggerMessage.tostPrint("Broker brofile null",context)
             var abx: IntentDailogbox =
                 IntentDailogbox(context, context.resources.getString(R.string.com_broker), "i")
             abx?.show()
             return false
         }

 //      else if(PreferenceManager.getUserData(context)?.isKycUploaded==false)
 //      {
 //          var abx: IntentDailogbox =  IntentDailogbox(context,context.resources.getString(R.string.com_kyc),"k")
 //          abx?.show()
 //          return false
 //      }

         else {
             return true
             // context.startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" +loadList.get(position).VNumber)));
         }
     }*/

    fun isNextDate(day: Int, month: Int, year: Int): Boolean {
        val calendar = Calendar.getInstance()
        val thisYear = calendar[Calendar.YEAR]
        Log.e("date", "# thisYear : $thisYear")
        val thisMonth = calendar[Calendar.MONTH]
        Log.e("date", "@ thisMonth : $thisMonth")
        val thisDay = calendar[Calendar.DAY_OF_MONTH]
        Log.e("date", "$ thisDay : $thisDay")
        val newthisMonth = thisMonth + 1

        // val sdf = SimpleDateFormat("dd/MM/yyyy")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
//dates to be compare
//dates to be compare
        var dd: String = "" + day
        if (day < 10) {
            dd = "0" + day
        }
        var my_date: String = "$year-$month-$dd"
        var curDate: String = PreferenceManager.getDateFormet(
            "yyyy-MM-dd",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            PreferenceManager.getCurrentDateTime()
        )
        val dateMy = sdf.parse(my_date)
        val datecut = sdf.parse(curDate)
        val strDate = sdf.parse(my_date)
        Log.e("date", "$ this date : $my_date")

        Log.e(
            " curant date",
            "$ this curant : ${
                PreferenceManager.getDateFormet(
                    "yyyy-MM-dd",
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    PreferenceManager.getCurrentDateTime()
                )
            }"
        )
        Log.e(
            "date mim",
            "$ this date : " + System.currentTimeMillis() + " slect min " + strDate.time
        )

        if (dateMy.compareTo(datecut) > 0) {
            System.out.println("Date 1 comes after Date 2");
            return true
        } else if (dateMy.compareTo(datecut) < 0) {
            System.out.println("Date 1 comes before Date 2");
            return false
        } else {
            return true

        }
//        if (System.currentTimeMillis() > strDate.time) {
//
//          return  false
//        }
//        else {
//          return   true
//        }

//        if (year > thisYear) {
//
//            return true
//        }
//        return if (year == thisYear && month >= newthisMonth && day >= thisDay) {
//           return true
//        } else{ return false
//        }

    }

    fun dateDiffranse(startDate: String, endDate: String, date_formate: String): Int {
        val currentDate = startDate
        val finalDate = endDate
        val date1: Date
        val date2: Date
        val dates = SimpleDateFormat(date_formate)
        date1 = dates.parse(currentDate)
        date2 = dates.parse(finalDate)
        val difference: Long = abs(date1.time - date2.time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        val dayDifference = differenceDates.toString()
        println("day Difference : $dayDifference")
        return dayDifference.toInt()
    }

    fun printDifference(startDate: Date, endDate: Date): Int {
        //milliseconds
        var different = endDate.time - startDate.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val numOfDays = (different / (1000 * 60 * 60 * 24)).toString().toInt()
        val hours = (different / (1000 * 60 * 60)).toString().toInt()
        val minutes = (different / (1000 * 60)).toString().toInt()
        val seconds = (different / 1000).toString().toInt()

        System.out.printf(
            "%d days, %d hours, %d minutes, %d seconds%n",
            numOfDays, hours, minutes, seconds
        )
        return numOfDays
    }

    @SuppressLint("SuspiciousIndentation")
    fun postLoadWindowDate(d: Int, dateFormet: String): String {
        val calendar: Calendar = GregorianCalendar()
        val sdf = SimpleDateFormat(dateFormet)

        calendar.add(Calendar.DATE, d)
        val day = sdf.format(calendar.time)
        Log.i("postLoadWindowDate", day)


        return day
    }


    fun getNextDateFromADate(d: Int, dateFormet: String, fromDate: String): String {

        val calendar: Calendar = GregorianCalendar()
        val sdf = SimpleDateFormat(dateFormet)
        val myDate: Date = sdf.parse(fromDate)
        calendar.time = myDate
        calendar.add(Calendar.DATE, d)
        val day = sdf.format(calendar.time)
        Log.i("postLoadWindowDate", day)

        return day
    }


    fun nextDay(day: Int, dateFormet: String): String {
        var md: String = ""
        val sdf = SimpleDateFormat(dateFormet)
        for (i in 1..day) {
            val calendar: Calendar = GregorianCalendar()
            calendar.add(Calendar.DATE, i)
            md = sdf.format(calendar.time)
            Log.i("My Next Date", md)
        }

        return md
    }


    /* @SuppressLint("SuspiciousIndentation")
     fun openTab(context: Context, url: String) {
         val builder = CustomTabsIntent.Builder()
         val params = CustomTabColorSchemeParams.Builder()
         params.setToolbarColor(ContextCompat.getColor(context, R.color.blue))
         builder.setDefaultColorSchemeParams(params.build())
         builder.setShowTitle(false)
         builder.enableUrlBarHiding()
         builder.setShareState(CustomTabsIntent.SHARE_STATE_OFF)
         builder.setInstantAppsEnabled(true)
         val customBuilder = builder.build()
         customBuilder.launchUrl(context, Uri.parse(url))

     }*/

    fun getVersion(context: Context): String {
        var verzon: String = ""
        try {
            val pInfo: PackageInfo =
                context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
            val version = pInfo.versionName
            verzon = "" + version;
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verzon
    }

    fun logRegToken(context: Context): String {
        //  LoggerMessage.LogErrorMsg("Start", "Firebace Token")
        // [START log_reg_token]
        var token: String = ""
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Token", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                token = task.result
                setFCM(token, context)
                // Log and toast
                val msg = "FCM Registration token: $token"
                Log.e("Token", msg)
                // Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            })
        // [END log_reg_token]
        return token
    }

    private fun currentDate(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }

    // Long: time in millisecond
    fun Long.toTimeAgo(): String {
        val SECOND = 1
        val MINUTE = 60 * SECOND
        val HOUR = 60 * MINUTE
        val DAY = 24 * HOUR
        val MONTH = 30 * DAY
        val YEAR = 12 * MONTH
        val time = this
        val now = currentDate()

        // convert back to second
        val diff = (now - time) / 1000

        return when {
            diff < MINUTE -> "Just now"
            diff < 2 * MINUTE -> "1 minute ago"
            diff < 60 * MINUTE -> "${diff / MINUTE} minutes ago"
            diff < 2 * HOUR -> "an hour ago"
            diff < 24 * HOUR -> "${diff / HOUR} hours ago"
            diff < 2 * DAY -> "yesterday"
            diff < 30 * DAY -> "${diff / DAY} days ago"
            diff < 2 * MONTH -> "a month ago"
            diff < 12 * MONTH -> "${diff / MONTH} months ago"
            diff < 2 * YEAR -> "a year ago"
            else -> "${diff / YEAR} years ago"
        }
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, timeStamp, null)
        LoggerMessage.LogErrorMsg("uri of logo", path)
        return Uri.parse(path)
    }

    fun isNextDateLoad(day: Int, month: Int, year: Int): Boolean {
        val calendar = Calendar.getInstance()
        val thisYear = calendar[Calendar.YEAR]
        Log.e("date", "# thisYear : $thisYear")
        val thisMonth = calendar[Calendar.MONTH]
        Log.e("date", "@ thisMonth : $thisMonth")
        val thisDay = calendar[Calendar.DAY_OF_MONTH]
        Log.e("date", "$ thisDay : $thisDay")
        val newthisMonth = thisMonth + 1

        val sdf = SimpleDateFormat("dd/MM/yyyy")

        var my_date: String = "$day/$month/$year"
        val strDate = sdf.parse(my_date)
        Log.e("date", "$ this date : $my_date")
        if (System.currentTimeMillis() > strDate.time) {
            return false
        } else {
            return true
        }
//        if (year > thisYear) {
//
//            return true
//        }
//        return if (year == thisYear && month >= newthisMonth && day >= thisDay) {
//           return true
//        } else{ return false
//        }
    }

    fun isAlartWindowDate(context: Context): Boolean {
        var cd: Int = getDateFormet("dd", "dd-MM-yyyy", getCurantDateTime()).toString().toInt()
        var wd: String = getAlartWindowDAte(context)

        LoggerMessage.LogErrorMsg("Curant Date", getCurantDateTime())

        if (wd.equals("n")) {
            return true
        }

        var wa: Int = wd.substring(0, 1).toInt()

        if (cd >= wa) {
            return true
        }


        return false
    }

    fun isNextDateBetween(
        day: Int,
        month: Int,
        year: Int,
        nextDate: String,
        formet: String
    ): Boolean {

        // val sdf = SimpleDateFormat("dd/MM/yyyy")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
//dates to be compare
//dates to be compare
        var dd: String = "" + day
        if (day < 10) {
            dd = "0" + day
        }
        var my_date: String = "$year-$month-$dd"
        var curDate: String = PreferenceManager.getDateFormet("yyyy-MM-dd", formet, nextDate)
        val dateMy = sdf.parse(my_date)
        val datecut = sdf.parse(curDate)
        val strDate = sdf.parse(my_date)
        Log.e("date", "$ this date : $my_date")

        Log.e(" curant date", "$ this curant : ${curDate}")
        Log.e(
            "date mim",
            "$ this date : " + System.currentTimeMillis() + " slect min " + strDate.time
        )

        if (dateMy.compareTo(datecut) > 0) {
            System.out.println("Date 1 comes after Date 2");
            return true
        } else if (dateMy.compareTo(datecut) < 0) {
            System.out.println("Date 1 comes before Date 2");
            return false
        } else {
            return true

        }

//        if (System.currentTimeMillis() > strDate.time) {
//
//          return  false
//        }
//        else {
//          return   true
//        }

//        if (year > thisYear) {
//
//            return true
//        }
//        return if (year == thisYear && month >= newthisMonth && day >= thisDay) {
//           return true
//        } else{ return false
//        }


    }

    fun getEpocToTime(time: Int): String {

        val format: DateFormat = SimpleDateFormat("mm:ss.SSS")
        val formatted = format.format(time)
        return formatted.toString()
    }

    fun bitmapTofile(imageBitmap: Bitmap, context: Context): File? {

        val bytes = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            imageBitmap,
            timeStamp + ".jpeg",
            null
        )
        LoggerMessage.LogErrorMsg("uri of logo", path)
        val file = getFile(context, Uri.parse(path))

        return file
    }

    @Throws(IOException::class)
    fun getFile(context: Context, uri: Uri?): File? {
        val destinationFilename =
            File(context.filesDir.path + File.separatorChar + queryName(context, uri!!))
        try {
            context.contentResolver.openInputStream(uri!!).use { ins ->
                createFileFromStream(
                    ins!!,
                    destinationFilename
                )
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
        return destinationFilename
    }

    private fun queryName(context: Context, uri: Uri): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    fun resizeImage(originalBitmap: Bitmap?, maxWidth: Int, maxHeight: Int): Bitmap? {
        var resizedBitmap: Bitmap? = null
        try {
            // Decode the image file into a Bitmap object
            // val originalBitmap = BitmapFactory.decodeFile(imagePath)

            // Get the original width and height of the image
            val originalWidth = originalBitmap?.width
            val originalHeight = originalBitmap?.height
            // Calculate the scale factor to resize the image while maintaining the aspect ratio
            val scaleFactor = Math.min(
                maxWidth.toFloat() / originalWidth!!,
                maxHeight.toFloat() / originalHeight!!
            )

            // Calculate the new width and height based on the scale factor
            val newWidth = Math.round(originalWidth * scaleFactor)
            val newHeight = Math.round(originalHeight * scaleFactor)

            // Resize the original bitmap to the new dimensions
            resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

            // Recycle the original bitmap to free up memory
            originalBitmap.recycle()
        } catch (e: Exception) {
            Log.e("ImageUtils", "Error resizing image: " + e.message)
        }
        return resizedBitmap
    }

    fun getPathFromUri(context: Context, uri: Uri?): String? {
        var filePath: String? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = cursor.getString(columnIndex)
                cursor.close()
            }
        } catch (e: java.lang.Exception) {
            Log.e("FileUtils", "Error getting path from URI: " + e.message)
        }
        return filePath
    }

    fun getServerDateUtc(): String {
        val date = Date() // current date and time
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getDefault()
        val isoDate = sdf.format(date).toString()
        return isoDate
    }

    fun getVehicleDetails(myContext: Context): String {
        val sharedPreferences: SharedPreferences =
            myContext.getSharedPreferences("VEHICLE_DETAILS_TABLE", MODE_PRIVATE)
        return sharedPreferences.getString("VEHICLE_DETAILS", null)!!
    }

    fun getUserData(finance: Activity): Any {
        // TODO("Not yet implemented")
        return 0
    }

    fun getAndroiDeviceId(context: Context): String {
        val androidId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return androidId
    }

}