package com.fpoly.sdeliverydriver.ui.main.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentOrderBinding
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewAction
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DeliveryOrderFragment : PolyBaseFragment<FragmentOrderBinding>() {
    private val homeViewModel:HomeViewModel by activityViewModel()
    private lateinit var tabLayout: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        initData()
    }

    private fun initData() {
        homeViewModel.handle(HomeViewAction.GetAllDeliveryOrders)
    }

    private fun setupAppBar() {
        views.appBar.tvTitleToolbar.text= "Lịch sử giao hàng"
    }

    override fun invalidate() {
        tabLayout = views.tabLayout
        val viewPager = views.viewPager
        val adapter = DeliveryOrderViewPagerAdapter(childFragmentManager, this.lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Giao thành công"
                }
                1 -> {
                    tab.text = "Đã hủy"
                }
            }
        }.attach()
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOrderBinding
            = FragmentOrderBinding.inflate(inflater, container, false)
}
