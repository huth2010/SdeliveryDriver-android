package com.fpoly.sdeliverydriver.ui.delivery

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.data.model.Notify
import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.databinding.FragmentMapsBinding
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.DELIVERING_STATUS
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.MAPVIEW_BUNDLE_KEY
import com.fpoly.sdeliverydriver.ultis.showSnackbar
import com.fpoly.sdeliverydriver.ultis.showUtilDialogWithCallback
import com.fpoly.sdeliverydriver.ultis.startToDetailPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest

class MapsFragment : PolyBaseFragment<FragmentMapsBinding>(), OnMapReadyCallback,
    GoogleMap.OnPoiClickListener,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
    OnMapClickListener, OnMarkerClickListener {

    private val deliveryViewModel: DeliveryViewModel by activityViewModel()
    private lateinit var googleMap: GoogleMap
    private val orderList = mutableListOf<OrderResponse>()
    private var mapFragment: SupportMapFragment? = null
    private val markerList = mutableListOf<Marker>()
    private var selectedOrder: OrderResponse? = null


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
        deliveryViewModel.handle(DeliveryViewAction.GetAllOrderByStatus(DELIVERING_STATUS))
    }

    private fun listenEvent() {
        views.appBar.btnBackToolbar.setOnClickListener {
            activity?.finish()
        }
        views.btnDetailOrder.setOnClickListener {
            deliveryViewModel.handle(DeliveryViewAction.GetCurrentOrder(selectedOrder!!._id))
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

    override fun onResume() {
        super.onResume()
        checkPermissionRound2()
    }

    private fun checkPermissionRound2() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.showUtilDialogWithCallback(
                Notify(
                    "Yêu cầu quyền",
                    "bạn chưa cho phép quyền sử dụng vị trí",
                    "Vào cài đặt để cấp quyền",
                    R.raw.animation_successfully
                )
            ) {
                activity?.startToDetailPermission()
            }
        }
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
        googleMap.setOnMarkerClickListener(this)
    }

    private fun handleMarkerClick(marker: Marker) {
        val markerPosition = marker.position
        selectedOrder = orderList.find { it.address.toLatLng() == markerPosition }
        if (selectedOrder != null) {
            setupOrderLayout(selectedOrder!!)
            handleSetAnimationCamera(markerPosition)
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
        showSnackbar(requireView(), """Clicked: ${poi.name}
            Place ID:${poi.placeId}
            Latitude:${poi.latLng.latitude} Longitude:${poi.latLng.longitude}""", true, null
        ) {}

    }

    override fun onMyLocationButtonClick(): Boolean {
        showSnackbar(requireView(), "Moving to your location", true, null) {}
        return true
    }

    override fun onMyLocationClick(location: Location) {
        showSnackbar(requireView(), "Current location:\n$location", true, null) {}
    }

    override fun onMapClick(p0: LatLng) {
        views.layoutAddress.visibility = View.GONE
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        handleMarkerClick(marker)
        return false
    }


    private fun setupLocationCustomerMarkers(orders: List<OrderResponse>) {
        markerList.forEach { it.remove() }
        markerList.clear()

        for (order in orders) {
            val orderLocation = order.address.toLatLng()
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(orderLocation)
                    .title(order.address.recipientName)
            )
            if (marker != null) {
                markerList.add(marker)
            }
            orderList.add(order)
        }
    }


    private fun handleSetAnimationCamera(latLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17F))
    }


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMapsBinding {
        return FragmentMapsBinding.inflate(inflater, container, false)
    }

    override fun invalidate() {
        withState(deliveryViewModel) {
            when (it.asyncDelivering) {
                is Success -> {
                    val orders = it.asyncDelivering.invoke() ?: emptyList()
                    val firstOrderLocation = orders.firstOrNull()?.address?.toLatLng()
                    if (firstOrderLocation != null) {
                        if (markerList.isEmpty()) {
                            handleSetAnimationCamera(firstOrderLocation)
                        }
                    }
                    setupLocationCustomerMarkers(orders)
                }

                is Fail -> {
                }

                else -> {
                }
            }
            when (it.asyncCreateDelivery) {
                is Success -> {
                    initData()
                    deliveryViewModel.handleRemoveAsyncCreateDelivery()
                }

                else -> {}
            }
        }
    }

}
