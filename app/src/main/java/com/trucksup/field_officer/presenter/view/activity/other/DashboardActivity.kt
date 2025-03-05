package com.trucksup.field_officer.presenter.view.activity.other

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.presenter.common.permission.PermissionResultCallback
import com.trucksup.field_officer.presenter.common.permission.PermissionUtils
import com.glovejob.data.model.PayloadXXXX
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityDashboardBinding
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity()  , ActivityCompat.OnRequestPermissionsResultCallback,
    PermissionResultCallback {
    private lateinit var allNavComponentList : List<PayloadXXXX>
    private lateinit var mBinding: ActivityDashboardBinding
    private var mViewModel: DashboardViewModel? = null
    private lateinit var permissionUtils: PermissionUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
       // val window = this.window
       // window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        setStatusBarColorAndIcons(this)
        mViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val statusId = intent.extras!!.getInt("status")
        val ft = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putInt("status", statusId)
        permissionUtils = PermissionUtils(this@DashboardActivity)
        permissionUtils.check_permission("k_app_required_permission", 9)

       // BaseApplication.loginState = statusId

     //   mViewModel!!.allPrivacydetails()

        setObserver()


        }

    private fun setObserver() {
        mViewModel!!.navAllResponseLD.observe(this) { resposemodel ->
            if (resposemodel.networkError != null) {

                Toast.makeText(
                    this, "" + resposemodel.networkError,
                    Toast.LENGTH_SHORT
                ).show()
            } else if (resposemodel.serverResponseError != null) {

                Toast.makeText(
                    this,
                    "" + resposemodel.serverResponseError, Toast.LENGTH_SHORT
                ).show()
            } else if (resposemodel.genericError != null) {

                Toast.makeText(
                    this,
                    "" + resposemodel.genericError,
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                if (resposemodel.success?.payload != null) {
                  allNavComponentList = resposemodel.success?.payload!!

                }
            }
        }
    }

    override fun PermissionGranted(request_code: Int) {
       // TODO("Not yet implemented")
    }

    override fun PartialPermissionGranted(
        request_code: Int,
        granted_permissions: ArrayList<String>
    ) {
        TODO("Not yet implemented")
    }

    override fun PermissionDenied(request_code: Int) {
        TODO("Not yet implemented")
    }

    override fun NeverAskAgain(request_code: Int) {
        TODO("Not yet implemented")
    }


    private fun showToastDialog(msg: String?, context: Context?, Ok: String?) {
        val deleteDialog = Dialog(context!!)
        deleteDialog.setContentView(R.layout.info_user)
        deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.window!!.setGravity(Gravity.CENTER)
        deleteDialog.setCancelable(true)
        val title = deleteDialog.findViewById<TextView>(R.id.msg_title)
        val yesButton = deleteDialog.findViewById<TextView>(R.id.ok_button)
        yesButton.text = Ok
        val cancelButton = deleteDialog.findViewById<ImageView>(R.id.cancel_button_dialog)
        title.text = msg
        yesButton.setOnClickListener {

            deleteDialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        cancelButton.setOnClickListener { view: View? ->

            deleteDialog.dismiss()
        }
        deleteDialog.show()
    }

    fun setStatusBarColorAndIcons(activity: AppCompatActivity) {
        val window = activity.window
        val colorPrimary = ContextCompat.getColor(activity, R.color.colorPrimary)
        window.statusBarColor = colorPrimary

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isLightColor = ColorUtils.calculateLuminance(colorPrimary) > 0.5
            var flags = window.decorView.systemUiVisibility
            flags = if (isLightColor) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            window.decorView.systemUiVisibility = flags
        }
    }



}