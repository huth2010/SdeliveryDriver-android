package com.fpoly.sdeliverydriver.ui.main.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest
import com.fpoly.sdeliverydriver.databinding.FragmentHomeBinding
import com.fpoly.sdeliverydriver.ui.delivery.DeliveryActivity
import com.fpoly.sdeliverydriver.ui.main.MainActivity
import com.fpoly.sdeliverydriver.ui.main.order.OrderAdapter
import com.fpoly.sdeliverydriver.ui.main.profile.UserViewModel
import javax.inject.Inject

class HomeFragment @Inject constructor() : PolyBaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by activityViewModel()

    private var adapter: OrderAdapter? = null

    val CONFIRMED_STATUS: String = "65264c102d9b3bb388078976"
    val DELIVERING_STATUS: String = "65264c672d9b3bb388078978"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listenEvent()
    }

    private fun init() {
        homeViewModel.handle(HomeViewAction.GetAllOrderByStatus(CONFIRMED_STATUS))
        homeViewModel.handle(HomeViewAction.GetAllOrderByStatus(DELIVERING_STATUS))
    }

    private fun listenEvent() {
        views.swipeLoading.setOnRefreshListener {
            homeViewModel.handle(HomeViewAction.GetAllOrderByStatus(CONFIRMED_STATUS))
            homeViewModel.handle(HomeViewAction.GetAllOrderByStatus(DELIVERING_STATUS))
        }
        views.btnDelivery.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),DeliveryActivity::class.java))
        }
    }

    private fun setupListOrder() {
        adapter = OrderAdapter { orderId ->
            homeViewModel.handle(
                HomeViewAction.UpdateOrderStatus(
                    orderId,
                    UpdateStatusRequest(DELIVERING_STATUS)
                )
            )
        }
        views.rcyOrder.adapter = adapter
    }

    private fun setupDeliveringLayout(quantity:Int) {
        views.layoutDelivery.visibility = View.VISIBLE
        views.orderQuantity.text= quantity.toString()
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun invalidate(): Unit = withState(homeViewModel) {
        views.swipeLoading.isRefreshing = it.isSwipeLoading
        when (it.asyncConfirmed) {
            is Success -> {
                setupListOrder()
                adapter?.setData(it.asyncConfirmed.invoke())
            }

            is Fail -> {

            }

            else -> {}
        }
        when (it.asyncUpdateOrderStatus) {
            is Success -> {
                homeViewModel.handle(HomeViewAction.GetAllOrderByStatus(CONFIRMED_STATUS))
                homeViewModel.handle(HomeViewAction.GetAllOrderByStatus(DELIVERING_STATUS))
                homeViewModel.handleRemoveAsyncUpdateOrderStatus()
            }

            is Fail -> {

            }

            else -> {}
        }
        when (it.asyncDelivering) {
            is Success -> {
                if (it.asyncDelivering.invoke()?.size!! > 0) {
                    setupDeliveringLayout(it.asyncDelivering.invoke()?.size!!)
                }else{
                    views.layoutDelivery.visibility = View.GONE
                }
            }
            is Fail -> {
                views.layoutDelivery.visibility = View.GONE
            }
            else -> {
                views.layoutDelivery.visibility = View.GONE
            }
        }

    }

}