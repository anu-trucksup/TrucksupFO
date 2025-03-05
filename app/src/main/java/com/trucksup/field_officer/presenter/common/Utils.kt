package com.trucksup.field_officer.presenter.common

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Utils {
    var strManualInstallation: String = ""

    fun showToastDialogLogin(msg: String?, context: Context) {
        val deleteDialog = Dialog(context)
        deleteDialog.setContentView(R.layout.info_user)
        deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.window!!.setGravity(Gravity.CENTER)
        deleteDialog.setCancelable(true)
        val title = deleteDialog.findViewById<TextView>(R.id.msg_title)
        val yesButton = deleteDialog.findViewById<TextView>(R.id.ok_button)
        val cancelButton = deleteDialog.findViewById<ImageView>(R.id.cancel_button_dialog)
        //title.setText(mViewModel.getLabel(AppConstant.AppLabelName.passwordUpdatedSuccessfully));
        title.text = msg
        yesButton.text = "Login"
        yesButton.setOnClickListener {
            context.startActivity(Intent(context, LoginActivity::class.java))
            deleteDialog.dismiss()
        }
        cancelButton.setOnClickListener { view: View? ->
            context.startActivity(
                Intent(
                    context,
                    LoginActivity::class.java
                )
            )
            //mProfileBinding.gender.setText(getString(R.string.female));
            deleteDialog.dismiss()
        }
        deleteDialog.show()
    }


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

    fun loadeESimImage(context: Context, view: ImageView, url: String?) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.progress_drawable_an)
            .error(R.drawable.esim_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .timeout(6000) // 60 second timeout
            .dontAnimate()
            .dontTransform()
        Glide.with(context).load(url).apply(options).into(view)
    }

    fun loadeESimImage1(context: Context, view: ImageView, url: String?) {
        val options = RequestOptions()
            .placeholder(R.drawable.progress_drawable_an)
            .error(R.drawable.esim_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
        Glide.with(context).load(url).apply(options).into(view)
    }

    fun loadImageCenterInside(context: Context, view: ImageView, url: String?) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.progress_drawable_an)
            .error(R.drawable.shopping_disabled)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform()
        Glide.with(context).load(url).apply(options).centerInside().into(view)
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(milliSeconds);
//        return formatter.format(calendar.getTime());
        return formatter.format(milliSeconds)
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
