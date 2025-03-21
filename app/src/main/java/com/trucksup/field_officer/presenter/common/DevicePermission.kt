package com.trucksup.field_officer.presenter.common

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import android.provider.Settings
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityDevicePermitioinsBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class DevicePermission : BaseActivity() {
    private lateinit var binding: ActivityDevicePermitioinsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_permitioins)
        checkPermition()
    }

    fun checkPermition() {
        var allowed: String = getString(R.string.allowed)
        var not_allowed: String = getString(R.string.not_allowed)
        if (ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.cam.text = not_allowed
        } else {
            binding.cam.text = allowed
        }

        if (ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.mic.text = not_allowed
        } else {
            binding.mic.text = allowed
        }
        if (ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.loc.text = not_allowed
        } else {
            binding.loc.text = allowed
        }
        if (ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.notification.text = not_allowed
        } else {
            binding.notification.text = allowed
        }
        if (ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.phone.text = not_allowed
        } else {
            binding.phone.text = allowed
        }

    }


    fun openDeviceSettings(v: View) {
        val packageName = packageName
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    fun backScreen(v: View) {
        finish()
    }
}