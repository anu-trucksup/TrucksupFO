package com.trucksup.field_officer.presenter.view.activity.truckSupplier.unassigned_ts_ba.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityUnassignedTsBaBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.unassigned_ts_ba.fragment.UnAssignedBAFragment
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.unassigned_ts_ba.fragment.UnAssignedTSFragment
import com.trucksup.field_officer.presenter.view.adapter.FragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnAssignedTSBAActivity : BaseActivity() {
    private lateinit var binding: ActivityUnassignedTsBaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_unassigned_ts_ba)
        setContentView(binding.root)

        setListener()
        setupViewPager()
    }

    private fun setListener() {
        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        //find button
        binding.tabUnassignedTs.setOnClickListener {
            binding.viewPager2.setCurrentItem(0, true)
        }

        //scheduled button
        binding.tabUnassignedBa.setOnClickListener {
            binding.viewPager2.setCurrentItem(1, true)
        }
    }

    private fun setupViewPager() {

        try {
            val adapter = FragmentAdapter(this)
            val fragment1: Fragment = UnAssignedTSFragment()
            val fragment2: Fragment = UnAssignedBAFragment()
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)
            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabUnassignedTs.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@UnAssignedTSBAActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabUnassignedBa.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@UnAssignedTSBAActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.textUnassignedTs.setTextColor(resources.getColor(R.color.white))
                        binding.textUnassignedBa.setTextColor(resources.getColor(R.color.blue))


                        /*binding.vLine1.setBackgroundColor(resources.getColor(R.color.blue))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.unselect_tab))*/
                        //binding.vLine3.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                    } else if (position == 1) {

                        binding.tabUnassignedBa.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@UnAssignedTSBAActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabUnassignedTs.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@UnAssignedTSBAActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.textUnassignedTs.setTextColor(resources.getColor(R.color.blue))
                        binding.textUnassignedBa.setTextColor(resources.getColor(R.color.white))


                        /*binding.vLine1.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.blue))*/
                    } else {
                        binding.tabUnassignedBa.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@UnAssignedTSBAActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabUnassignedTs.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@UnAssignedTSBAActivity,
                                R.drawable.tab_unselected_background
                            )
                        );
                        binding.textUnassignedTs.setTextColor(resources.getColor(R.color.white))
                        binding.textUnassignedBa.setTextColor(resources.getColor(R.color.blue))


                        /*binding.vLine1.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.unselect_tab))*/
                    }
                }
            })
        } catch (e: Exception) {

        }

    }
}