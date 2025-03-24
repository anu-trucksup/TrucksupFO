package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trucksup.field_officer.databinding.ActivityMyProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
    }

    private fun setListener() {

        binding.version.text = "Version: 1.0.0"

        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        //submit button
        binding.btnSubmit.setOnClickListener {
            finish()
        }

        //close button
        binding.btnClose.setOnClickListener {
            finish()
        }

        //camera or photo button
        binding.ivProfile.setOnClickListener {
            Toast.makeText(this@EditProfileActivity, "Edit Profile", Toast.LENGTH_SHORT).show()
        }

    }
}