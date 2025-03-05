package com.trucksup.field_officer.presenter.view.fragment.ts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.FragmentOwnerBinding
import com.trucksup.fieldofficer.adapter.FragmentAdapter

class TruckOwnerFragment : Fragment() {

    private lateinit var binding: FragmentOwnerBinding
    private var aContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            aContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOwnerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()

        setListener()
    }

    private fun setListener() {
       /* //find button
        binding.btnFind.setOnClickListener {
            binding.viewPager2.setCurrentItem(0, true)
        }*/

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
            val adapter = FragmentAdapter(requireActivity())
            //val fragment1: Fragment? = OwnerFindFragment()
            val fragment1: Fragment? = TruckOwnerScheduledFragment()
            val fragment2: Fragment? = OwnerCompletedFragment()
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)

            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabSchedule.setBackgroundDrawable(ContextCompat.getDrawable(context!!, R.drawable.ba_tab_unselected_background));
                        binding.tabCompleted.setBackgroundDrawable(ContextCompat.getDrawable(context!!, R.drawable.tab_selected_background));
                        binding.txtSchedule.setTextColor(resources.getColor(R.color.white))
                        binding.txtComplete.setTextColor(resources.getColor(R.color.blue))

                    } else if (position == 1) {

                        binding.tabCompleted.setBackgroundDrawable(ContextCompat.getDrawable(context!!, R.drawable.ba_tab_unselected_background));
                        binding.tabSchedule.setBackgroundDrawable(ContextCompat.getDrawable(context!!, R.drawable.tab_selected_background));
                        binding.txtSchedule.setTextColor(resources.getColor(R.color.blue))
                        binding.txtComplete.setTextColor(resources.getColor(R.color.white))

                    }
                }
            })
        } catch (e: Exception) {

        }

    }

}