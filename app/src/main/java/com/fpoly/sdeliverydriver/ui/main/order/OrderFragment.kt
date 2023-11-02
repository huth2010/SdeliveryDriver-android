package com.fpoly.sdeliverydriver.ui.main.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.databinding.FragmentOrderBinding
import com.fpoly.sdeliverydriver.ui.main.profile.UserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderFragment : PolyBaseFragment<FragmentOrderBinding>() {
    private lateinit var tabLayout: TabLayout
   // private val productViewModel: ProductViewModel by activityViewModel()
    private val userViewModel: UserViewModel by activityViewModel()

    companion object {
        const val TAG = "OrderFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = withState(userViewModel){
            it.asyncCurrentUser.invoke()?._id
        }
        if (userId!=null){
          //  productViewModel.handle(ProductAction.GetAllOrderByUserId(userId)) //"6526a6e6adce6a54f6f67d7d"
            //"653bc0a72006e5791beab35b"
        }
    }
    override fun invalidate() {
        tabLayout = views.tabLayout
        val viewPager = views.viewPager
        val adapter = OrderViewPagerAdapter(childFragmentManager, this.lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Chờ"
                }
                1 -> {
                    tab.text = "Xác nhận"
                }
                2 -> {
                    tab.text = "Đang giao"
                }
                3 -> {
                    tab.text = "Lịch sử"
                }
            }
        }.attach()
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOrderBinding
            = FragmentOrderBinding.inflate(inflater, container, false)
}
