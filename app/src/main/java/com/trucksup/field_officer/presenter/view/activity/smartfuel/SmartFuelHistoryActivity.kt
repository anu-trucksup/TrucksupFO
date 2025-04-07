package com.trucksup.field_officer.presenter.view.activity.smartfuel

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityFinanceHistoryBinding
import com.trucksup.field_officer.databinding.ActivitySmartfuelHistoryBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.FragmentAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SmartFuelHistoryActivity : BaseActivity() {

    private lateinit var binding: ActivitySmartfuelHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_smartfuel_history)

        setListener()
        setupViewPager()

    }

    private fun setListener() {

        //back button
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        //active button
        binding.tabActive.setOnClickListener {
            binding.viewPager2.setCurrentItem(0, true)
        }

        //completed button
        binding.tabCompleted.setOnClickListener {
            binding.viewPager2.setCurrentItem(1, true)
        }

        //reject button
        binding.tabRejected.setOnClickListener {
            binding.viewPager2.setCurrentItem(2, true)
        }
    }

    private fun setupViewPager() {

        try {
            val adapter = FragmentAdapter(this)
            val fragment1 = HistorySmartFuelFragment("active")
            val fragment2 = HistorySmartFuelFragment("complete")
            val fragment3 = HistorySmartFuelFragment("reject")
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)
            adapter.addFragment(fragment3)
            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.white))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.blue))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.blue))

                    } else if (position == 1) {

                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        )
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.blue))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.white))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.blue))
                    } else if (position == 2) {
                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_unselected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.blue))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.blue))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.white))
                    }
                }
            })
        } catch (_: Exception) {
        }

    }

    fun addNewEnquiry(view: View) {
        val intent = Intent(this, AddSmartFuelActivity::class.java)
        startActivity(intent)

    }


}