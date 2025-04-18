package com.trucksup.field_officer.presenter.view.activity.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.databinding.ActivityMyTeamsBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.MyBATeamActivity
import com.trucksup.field_officer.presenter.view.activity.growthPartner.GPViewAllActivity
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.MyTSTeamActivity

class MyTeamScreen : BaseActivity() {
    private lateinit var binding: ActivityMyTeamsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTeamsBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    fun clickOnTS(view: View) {
        startActivity(Intent(this, MyTSTeamActivity::class.java))
    }

    fun clickOnBA(view: View) {
        startActivity(Intent(this, MyBATeamActivity::class.java))
    }

    fun clickOnGP(view: View) {
        startActivity(Intent(this, GPViewAllActivity::class.java))
    }
}