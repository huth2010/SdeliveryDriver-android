package com.fpoly.sdeliverydriver.ui.main.order

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrderViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return CurrentOrdersFragment()
            1 -> return ComfirmedFragment()
            2 -> return HistoryOrderFragment()
        }
        return CurrentOrdersFragment();
    }

}