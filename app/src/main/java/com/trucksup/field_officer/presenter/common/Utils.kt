package com.trucksup.field_officer.presenter.common

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


object Utils {
    var strManualInstallation: String = ""

    fun appSettingOpen(context: Context) {
        /*Toast.makeText(
            context,
            "Go to Setting and Enable All Permission",
            Toast.LENGTH_LONG
        ).show()*/

        Toast.makeText(
            context,
            "Go to Setting and Enable Camera Permission",
            Toast.LENGTH_LONG
        ).show()

        val settingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        settingIntent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(settingIntent)
    }

    fun warningPermissionDialog(context: Context, listener: DialogInterface.OnClickListener) {
        MaterialAlertDialogBuilder(context)
            .setMessage("All Permission are Required for this app")
            .setCancelable(false)
            .setPositiveButton("Ok", listener)
            .create()
            .show()
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun Glide.showImage(context: Context, imageView: ImageView, url: String?) {
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.placeholder_image2)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform()

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    const val ORIENT_PORTRAIT = 0
    const val ORIENT_LANDSCAPE_LEFT = 1
    const val ORIENT_LANDSCAPE_RIGHT = 2


    fun showToastDialog(msg: String?, context: Context, Ok: String?) {
        val deleteDialog = Dialog(context)
        deleteDialog.setContentView(R.layout.info_user)
        deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.window!!.setGravity(Gravity.CENTER)
        deleteDialog.setCancelable(false)
        val title = deleteDialog.findViewById<TextView>(R.id.msg_title)
        val yesButton = deleteDialog.findViewById<TextView>(R.id.ok_button)
        yesButton.text = Ok
        val cancelButton = deleteDialog.findViewById<ImageView>(R.id.cancel_button_dialog)
        //title.setText(mViewModel.getLabel(AppConstant.AppLabelName.passwordUpdatedSuccessfully));
        title.text = msg
        yesButton.setOnClickListener { // mProfileBinding.gender.setText(getString(R.string.male));
            // Toast.makeText(EditProfileActivity.this, "Account Deleted!!", Toast.LENGTH_SHORT).show();
            deleteDialog.dismiss()
        }
        cancelButton.setOnClickListener { view: View? ->
            //mProfileBinding.gender.setText(getString(R.string.female));
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
            flags =
                if (isLightColor) flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            window.decorView.systemUiVisibility = flags
        }
    }


    fun loadImageQr(context: Context, view: ImageView, url: String?) {
        val options = RequestOptions()
            .placeholder(R.drawable.progress_drawable_an)
            .error(R.drawable.proper_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform()
        Glide.with(context).load(url).apply(options).into(view)

        //ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        //imageLoader.displayImage(url, view);
    }

    fun loadImage(context: Context, view: ImageView, url: String?) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.progress_drawable_an)
            .error(R.drawable.shopping_disabled)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform()
        Glide.with(context).load(url).apply(options).into(view)

        //ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        //imageLoader.displayImage(url, view);
    }

    fun getDateFormatted(dateString: String?): String {
        if (dateString != null) {
            try {
                val firstApiFormat = SimpleDateFormat("dd-mm-yyyy HH:mm:ss").parse(dateString)
                return SimpleDateFormat("dd MMM yyyy").format(firstApiFormat)
            } catch (er: Exception) {
                er.printStackTrace()
            }
        }
        return ""
    }

    fun getDateMillis(dateString: String?): Long {
        if (dateString != null) {
            try {
                val firstApiFormat = SimpleDateFormat("dd-mm-yyyy HH:mm:ss").parse(dateString)
                return firstApiFormat!!.time
            } catch (er: Exception) {
                er.printStackTrace()
            }
        }
        return 0
    }
}
