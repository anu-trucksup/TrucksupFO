package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.databinding.ActivityMyProfileBinding
import com.trucksup.field_officer.presenter.common.AppVersionUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.profile.vml.EditProfileViewModel
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.vml.TSOnboardViewModel

class EditProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityMyProfileBinding
    private var mViewModel: EditProfileViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        setListener()
    }

    private fun setListener() {

        binding.version.text = AppVersionUtils.getAppVersionName(this)

        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        //submit button
        binding.btnSubmit.setOnClickListener {

        }

        //close button
        binding.btnClose.setOnClickListener {
           onBackPressed()
        }

        //camera or photo button
        binding.ivProfile.setOnClickListener {
            Toast.makeText(this@EditProfileActivity, "Edit Profile", Toast.LENGTH_SHORT).show()
        }

    }
}