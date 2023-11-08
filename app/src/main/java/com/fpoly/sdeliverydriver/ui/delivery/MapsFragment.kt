package com.fpoly.sdeliverydriver.ui.delivery

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.databinding.FragmentMapsBinding
import com.fpoly.sdeliverydriver.ui.main.home.HomeFragment.Companion.DELIVERING_STATUS
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewAction
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewModel
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.MAPVIEW_BUNDLE_KEY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest

class MapsFragment : PolyBaseFragment<FragmentMapsBinding>(), OnMapReadyCallback,
    GoogleMap.OnPoiClickListener,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapClickListener {

    private val homeViewModel: HomeViewModel by activityViewModel()
    private lateinit var googleMap: GoogleMap
    private val orderList = mutableListOf<OrderResponse>()
    private var mapFragment: SupportMapFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        setupAppBar()
        initData()
        listenEvent()
    }

    private fun initMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun setupAppBar() {
        views.appBar.apply {
            btnBackToolbar.visibility = View.VISIBLE
            tvTitleToolbar.text = getString(R.string.delivery)
        }
    }

    private fun initData() {
        homeViewModel.handle(HomeViewAction.GetAllOrderByStatus(DELIVERING_STATUS))
    }

    private fun listenEvent() {
        views.appBar.btnBackToolbar.setOnClickListener {
            activity?.finish()
        }
        views.btnDetailOrder.setOnClickListener {
        findNavController().navigate(R.id.action_mapsFragment_to_deliveryOrderDetailFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mapFragment?.onSaveInstanceState(mapViewBundle)
    }

    override fun onStart() {
        super.onStart()
        mapFragment?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapFragment?.onStop()
    }

    override fun onPause() {
        mapFragment?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapFragment?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapFragment?.onLowMemory()
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setOnPoiClickListener(this)
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        googleMap.setOnMapClickListener(this)
        googleMap.setOnMarkerClickListener { marker ->
            handleMarkerClick(marker)
            false
        }
    }

    private fun handleMarkerClick(marker: Marker) {
        val markerPosition = marker.position
        val selectedOrder = orderList.find { it.address.toLatLng() == markerPosition }
        if (selectedOrder != null) {
            setupOrderLayout(selectedOrder)
            homeViewModel.handle(HomeViewAction.GetCurrentOrder(selectedOrder._id))
        }
    }

    private fun setupOrderLayout(selectedOrder: OrderResponse) {
        views.apply {
            layoutAddress.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(selectedOrder.userId.avatar?.url)
                .placeholder(R.drawable.baseline_person_outline_24)
                .error(R.drawable.baseline_person_outline_24)
                .into(imgAvatar)
            recipientName.text = selectedOrder.address.recipientName
            phone.text = selectedOrder.address.phoneNumber
            address.text = selectedOrder.address.addressLine
        }
    }

    override fun onPoiClick(poi: PointOfInterest) {
        Toast.makeText(
            requireContext(), """Clicked: ${poi.name}
            Place ID:${poi.placeId}
            Latitude:${poi.latLng.latitude} Longitude:${poi.latLng.longitude}""",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireContext(), "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(requireContext(), "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    override fun onMapClick(p0: LatLng) {
        views.layoutAddress.visibility = View.GONE
    }

    private fun setupLocationCustomerMarkers(orders: List<OrderResponse>) {
        for (order in orders) {
            val orderLocation = order.address?.toLatLng()
            if (orderLocation != null) {
                val marker = MarkerOptions()
                    .position(orderLocation)
                    .title(order.address.recipientName)
                googleMap.addMarker(marker)
                orderList.add(order)
            }
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMapsBinding {
        return FragmentMapsBinding.inflate(inflater, container, false)
    }

    override fun invalidate() {
        withState(homeViewModel) {
            when (it.asyncDelivering) {
                is Success -> {
                    val orders = it.asyncDelivering.invoke() ?: emptyList()
                    val firstOrderLocation = orders.firstOrNull()?.address?.toLatLng()
                    if (firstOrderLocation != null) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstOrderLocation, 17F))
                        setupLocationCustomerMarkers(orders)
                    }
                }
                is Fail -> {
                }
                else -> {
                }
            }
        }
    }
}
