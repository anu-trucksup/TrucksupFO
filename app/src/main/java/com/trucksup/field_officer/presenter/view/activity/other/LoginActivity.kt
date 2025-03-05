package com.trucksup.field_officer.presenter.view.activity.other

import android.content.Intent
import android.os.Bundle
import com.trucksup.field_officer.databinding.ActivityLoginBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)

       // binding.versionName.text = "Version : " + BuildConfig.VERSION_NAME

        setOnListeners()
    }

    private fun setOnListeners() {
        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, WelcomeLocationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}