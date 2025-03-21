package com.trucksup.field_officer.presenter.common.image_picker

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import com.trucksup.field_officer.R

class ImagePickerDailog(var context: Activity, var getImage: GetImage) : Dialog(context) {

    private var gallery: LinearLayout? = null
    private var camera: LinearLayout? = null

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