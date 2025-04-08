package com.trucksup.field_officer.presenter.common.image_picker

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImagePickerDailog(var context: Activity, var getImage: GetImage) : Dialog(context) {

    private var gallery: LinearLayout? = null
    private var camera: LinearLayout? = null
    private val fragment: Fragment? = null
    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  this.getWindow()?.setFlags(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        setContentView(R.layout.image_picker_dailog)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        );
        this.window?.setLayout(
            ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.FILL_PARENT
        );

        this.setCancelable(true)
        init()
    }

    @SuppressLint("NewApi")
    fun init() {

        this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        gallery = findViewById(R.id.gallery)
        camera = findViewById(R.id.camra)

        gallery?.setOnClickListener {
            getImage.fromGallery()
            this.dismiss()

        }
        camera?.setOnClickListener {
            getImage.fromCamara()
            this.dismiss()
        }



    }

}