package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.GpFollowupActivityBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAFollowUpViewModel
import com.trucksup.field_officer.presenter.view.activity.growthPartner.vml.GPFollowUpViewModel
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetupTSRequest
import com.trucksup.field_officer.presenter.view.fragment.gp.GPCompletedFragment
import com.trucksup.field_officer.presenter.view.fragment.gp.GPScheduledFragment
import com.trucksup.field_officer.presenter.view.adapter.FragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GPFollowupActivity : BaseActivity() {

    private lateinit var binding: GpFollowupActivityBinding
    //private var mViewModel: GPFollowUpViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GpFollowupActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        //mViewModel = ViewModelProvider(this)[GPFollowUpViewModel::class.java]

        //showProgressDialog(this, false)

        //mViewModel?.getAllMeetupGP(PreferenceManager.getAuthToken(), request)

        setupViewPager()

        setListener()
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        //scheduled button
        binding.tabSchedule.setOnClickListener {
            binding.viewPager2.setCurrentItem(0, true)
        }

        //completed button
        binding.tabCompleted.setOnClickListener {
            binding.viewPager2.setCurrentItem(1, true)
        }
    }

    private fun setupViewPager() {

        try {
            val adapter = FragmentAdapter(this)
            val fragment1: Fragment = GPScheduledFragment()
            val fragment2: Fragment = GPCompletedFragment()
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)

            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabSchedule.setBackgroundDrawable(ContextCompat.getDrawable(this@GPFollowupActivity, R.drawable.ba_tab_unselected_background));
                        binding.tabCompleted.setBackgroundDrawable(ContextCompat.getDrawable(this@GPFollowupActivity, R.drawable.tab_selected_background));
                        binding.txtSchedule.setTextColor(resources.getColor(R.color.white))
                        binding.txtComplete.setTextColor(resources.getColor(R.color.blue))

                    } else if (position == 1) {

                        binding.tabCompleted.setBackgroundDrawable(ContextCompat.getDrawable(this@GPFollowupActivity, R.drawable.ba_tab_unselected_background));
                        binding.tabSchedule.setBackgroundDrawable(ContextCompat.getDrawable(this@GPFollowupActivity, R.drawable.tab_selected_background));
                        binding.txtSchedule.setTextColor(resources.getColor(R.color.blue))
                        binding.txtComplete.setTextColor(resources.getColor(R.color.white))

                    }
                }
            })
        } catch (e: Exception) {

        }

    }

}