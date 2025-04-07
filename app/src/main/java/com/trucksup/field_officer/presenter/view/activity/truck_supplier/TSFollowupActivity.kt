package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.TsFollowupActivityBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.fragment.ts.TSCompletedFragment
import com.trucksup.field_officer.presenter.view.fragment.ts.TSScheduledFragment
import com.trucksup.field_officer.presenter.view.adapter.FragmentAdapter

class TSFollowupActivity : BaseActivity() {

    private lateinit var binding: TsFollowupActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TsFollowupActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        setupViewPager()

        setListener()
    }

    private fun setListener() {

        //scheduled button
        binding.tabSchedule.setOnClickListener {
            binding.viewPager2.setCurrentItem(0, true)
        }

        //completed button
        binding.tabCompleted.setOnClickListener {
            binding.viewPager2.setCurrentItem(1, true)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViewPager() {

        try {
            val adapter = FragmentAdapter(this)

            val fragment1: Fragment = TSScheduledFragment()
            val fragment2: Fragment = TSCompletedFragment()
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)

            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabSchedule.setBackgroundDrawable(ContextCompat.getDrawable(this@TSFollowupActivity, R.drawable.ba_tab_unselected_background));
                        binding.tabCompleted.setBackgroundDrawable(ContextCompat.getDrawable(this@TSFollowupActivity, R.drawable.tab_selected_background));
                        binding.txtSchedule.setTextColor(resources.getColor(R.color.white))
                        binding.txtComplete.setTextColor(resources.getColor(R.color.blue))

                    } else if (position == 1) {

                        binding.tabCompleted.setBackgroundDrawable(ContextCompat.getDrawable(this@TSFollowupActivity, R.drawable.ba_tab_unselected_background));
                        binding.tabSchedule.setBackgroundDrawable(ContextCompat.getDrawable(this@TSFollowupActivity, R.drawable.tab_selected_background));
                        binding.txtSchedule.setTextColor(resources.getColor(R.color.blue))
                        binding.txtComplete.setTextColor(resources.getColor(R.color.white))

                    }
                }
            })
        } catch (_: Exception) {

        }

    }

}