package com.trucksup.field_officer.presenter.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private var fragments = arrayListOf<Fragment>()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        try {
            return fragments[position]
        } catch (e: Exception) {
            return fragments[position]
        }
    }

    fun addFragment(fragment: Fragment?) {
        try {
            if (fragment != null) {
                fragments.add(fragment)
            }
        } catch (e: Exception) {

        }
    }
}
