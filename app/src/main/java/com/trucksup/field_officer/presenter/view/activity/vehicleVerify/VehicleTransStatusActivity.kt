package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityVehicleTransStatusBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter.PurchasedPlanAdap

class VehicleTransStatusActivity : BaseActivity() {

    private lateinit var binding: ActivityVehicleTransStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_trans_status)

        setPlanList()
    }

    private fun setPlanList() {
        var list = ArrayList<String>()
        list.add("")
        binding.planList.apply {
            layoutManager =
                LinearLayoutManager(this@VehicleTransStatusActivity, RecyclerView.VERTICAL, false)
            adapter = PurchasedPlanAdap(this@VehicleTransStatusActivity, list)
            hasFixedSize()
        }
    }


    fun closeScreen(v: View) {
        finish()
    }

}