package com.trucksup.field_officer.presenter.view.fragment.ba

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.FragmentBrokerBinding
import com.trucksup.fieldofficer.adapter.FragmentAdapter

class BrokerFragment : Fragment() {

    private lateinit var binding: FragmentBrokerBinding
    private var aContext:Context?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity)
        {
            aContext=context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentBrokerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()

        setListener()
    }

    private fun setListener()
    {
        //find button
        binding.btnFind.setOnClickListener {
            binding.viewPager2.setCurrentItem(0, true)
        }

        //scheduled button
        binding.btnScheduled.setOnClickListener{
            binding.viewPager2.setCurrentItem(1, true)
        }

        //completed button
        binding.btnCompleted.setOnClickListener {
            binding.viewPager2.setCurrentItem(2, true)
        }
    }

    private fun setupViewPager() {

        try {
            val adapter = FragmentAdapter(requireActivity())
            val fragment1: Fragment? = BrokerFindFragment()
            val fragment2: Fragment? = BrokerScheduledFragment()
            val fragment3: Fragment? = BrokerCompletedFragment()
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)
            adapter.addFragment(fragment3)
            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled=false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.vLine1.setBackgroundColor(resources.getColor(R.color.blue))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine3.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                    } else if (position == 1) {
                        binding.vLine1.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.blue))
                        binding.vLine3.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                    } else {
                        binding.vLine1.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine3.setBackgroundColor(resources.getColor(R.color.blue))
                    }
                }
            })
        } catch (e: Exception) {

        }

    }



}