package com.trucksup.field_officer.presenter.view.activity.business_associate

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityBusinessAssociatesNewBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.fragment.ba.ActiveBAFragment
import com.trucksup.field_officer.presenter.view.fragment.ba.InActiveBAFragment
import com.trucksup.fieldofficer.adapter.FragmentAdapter

class BAStatusViewAllActivity : BaseActivity() {
    private lateinit var binding: ActivityBusinessAssociatesNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_business_associates_new)
        val view = binding.root
        setContentView(view)

        setListener()
        setupViewPager()
    }

    private fun setListener() {
        //find button
        binding.tabActiveBA.setOnClickListener {
            binding.viewPager2.setCurrentItem(0, true)
        }

        //scheduled button
        binding.tabInActiveBA.setOnClickListener {
            binding.viewPager2.setCurrentItem(1, true)
        }

        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViewPager() {

        try {
            val adapter = FragmentAdapter(this)
            val fragment1: Fragment = ActiveBAFragment()
            val fragment2: Fragment = InActiveBAFragment()
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)
            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BAStatusViewAllActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabInActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BAStatusViewAllActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.txtActiveBA.setTextColor(resources.getColor(R.color.white))
                        binding.txtInActiveBA.setTextColor(resources.getColor(R.color.blue))


                        /*binding.vLine1.setBackgroundColor(resources.getColor(R.color.blue))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.unselect_tab))*/
                        //binding.vLine3.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                    } else if (position == 1) {

                        binding.tabInActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BAStatusViewAllActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BAStatusViewAllActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.txtActiveBA.setTextColor(resources.getColor(R.color.blue))
                        binding.txtInActiveBA.setTextColor(resources.getColor(R.color.white))


                        /*binding.vLine1.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.blue))*/
                    } else {
                        binding.tabInActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BAStatusViewAllActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BAStatusViewAllActivity,
                                R.drawable.tab_unselected_background
                            )
                        );
                        binding.txtActiveBA.setTextColor(resources.getColor(R.color.white))
                        binding.txtInActiveBA.setTextColor(resources.getColor(R.color.blue))


                        /*binding.vLine1.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.unselect_tab))*/
                    }
                }
            })
        } catch (e: Exception) {

        }

    }
}