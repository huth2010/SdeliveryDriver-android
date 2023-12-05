package com.fpoly.sdeliverydriver.ui.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.data.model.Notify
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest
import com.fpoly.sdeliverydriver.data.model.UserLocation
import com.fpoly.sdeliverydriver.databinding.FragmentHomeBinding
import com.fpoly.sdeliverydriver.ui.delivery.DeliveryActivity
import com.fpoly.sdeliverydriver.ui.main.order.adapter.OrderAdapter
import com.fpoly.sdeliverydriver.ui.main.profile.UserViewModel
import com.fpoly.sdeliverydriver.ui.notification.LocationService
import com.fpoly.sdeliverydriver.ultis.Constants
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.CONFIRMED_STATUS
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.DELIVERING_STATUS
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.collection_user_locations
import com.fpoly.sdeliverydriver.ultis.checkRequestPermissions
import com.fpoly.sdeliverydriver.ultis.showSnackbar
import com.fpoly.sdeliverydriver.ultis.showUtilDialogWithCallback
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.io.IOException
import javax.inject.Inject

class HomeFragment @Inject constructor() : PolyBaseFragment<FragmentHomeBinding>(),
    OnMapReadyCallback {

    private val homeViewModel: HomeViewModel by activityViewModel()
    private val userViewModel: UserViewModel by activityViewModel()

    private var adapter: OrderAdapter? = null

    companion object {
        const val TAG = "HomeFragment"
    }

    private lateinit var mDb: FirebaseFirestore
    private lateinit var mGoogleMap: GoogleMap
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mUserLocation: UserLocation? = null
    private var mLocationPermissionGranted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setupLocation()
        listenEvent()
        checkGoogleServicesAndGPS()
    }

    private fun checkGoogleServicesAndGPS() {
        if (isServicesOK() && isMapsEnabled()) {
            if (mLocationPermissionGranted) {
                getUserDetails()
            } else {
                getLocationPermission()
            }
        }
    }

    override fun onResume() {
        initData()
        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                getUserDetails()
            } else {
                getLocationPermission()
            }
        }
        super.onResume()
    }

    private fun setupAppBar(location: String) {
        views.currentLocation.text = location
    }

    private fun getLocationPermission() {
        checkRequestPermissions {
            mLocationPermissionGranted = it
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PERMISSIONS_REQUEST_ENABLE_GPS) {
            checkGoogleServicesAndGPS()
        }
    }

    private fun getUserDetails() {
        if (mUserLocation == null) {
            val user = withState(userViewModel) {
                it.asyncCurrentUser.invoke()
            }
            mUserLocation = UserLocation(user, null, null)
            (activity?.application as PolyApplication).setUser(user)
            getLastKnowLocation()
        } else {
            getLastKnowLocation()
        }
    }

    private fun checkMapServices(): Boolean {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true
            }
        }
        return false
    }

    private fun isMapsEnabled(): Boolean {
        val manager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }

    private fun buildAlertMessageNoGps() {
        activity?.showUtilDialogWithCallback(
            Notify(
                "Driver requires GPS to work properly",
                "",
                "Do you want to enable it?",
                R.raw.animation_successfully
            )
        )
        {
            val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(enableGpsIntent, Constants.PERMISSIONS_REQUEST_ENABLE_GPS)
        }
    }

    private fun isServicesOK(): Boolean {
        val available =
            activity?.applicationContext?.let {
                GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                    it
                )
            }

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google Play Services is working")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available!!)) {
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it")
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(requireActivity(), available, Constants.ERROR_DIALOG_REQUEST)
            dialog!!.show()
        } else {
            Toast.makeText(requireActivity(), "You can't make map requests", Toast.LENGTH_SHORT)
                .show()
        }
        return false
    }

    private fun getLastKnowLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mFusedLocationProviderClient?.lastLocation?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val location = task.result
                if (location != null) {
                    val geoPoint = GeoPoint(location.latitude, location.longitude)
                    homeViewModel.handle(
                        HomeViewAction.GetCurrentLocation(
                            location.latitude,
                            location.longitude
                        )
                    )
                    mUserLocation?.apply {
                        this.geoPoint = geoPoint
                        this.timestamp = null
                    }
                    handleGetAddressLocation()
                    saveUserLocation()
                    startLocationService()
                } else {
                    Log.e(TAG, "getLastKnowLocation: Last known location is null")
                }
            } else {
                Log.e(TAG, "getLastKnowLocation: Failed to get last known location")
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun handleGetAddressLocation() {
        if (mUserLocation != null) {
            val latLng =
                LatLng(mUserLocation!!.geoPoint!!.latitude, mUserLocation!!.geoPoint!!.longitude)
            mGoogleMap.clear()
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
            mGoogleMap.addMarker(MarkerOptions().position(latLng))
        }
    }

    private fun saveUserLocation() {
        if (mUserLocation != null) {
            val locationRef: DocumentReference = mDb
                .collection(collection_user_locations)
                .document(mUserLocation!!.user?._id.toString())
            locationRef.set(mUserLocation!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(
                        TAG,
                        "saveUserLocation: ${mUserLocation?.geoPoint?.latitude} -${mUserLocation?.geoPoint?.longitude}"
                    )
                }
            }
        }
    }

    private fun startLocationService() {
        if (!isLocationServiceRunning()) {
            val serviceIntent = Intent(requireActivity(), LocationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity?.startForegroundService(serviceIntent)
            } else {
                activity?.startService(serviceIntent)
            }
        }
    }

    private fun isLocationServiceRunning(): Boolean {
        val manager =
            activity?.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("com.fpoly.sdeliverydriver.ui.notification.LocationService" == service.service.className) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.")
                return true
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.")
        return false
    }

    private fun setupLocation() {
        mDb = FirebaseFirestore.getInstance()
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map_view_home) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this)
    }

    private fun initData() {
        homeViewModel.handle(HomeViewAction.GetAllOrderByStatus(CONFIRMED_STATUS))
        homeViewModel.handle(HomeViewAction.GetAllOrderByStatus(DELIVERING_STATUS))
    }

    private fun listenEvent() {
        views.swipeLoading.setOnRefreshListener {
            initData()
        }
        views.btnDelivery.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), DeliveryActivity::class.java))
        }
    }

    private fun setupListOrder() {
        val shipperId = withState(userViewModel) {
            it.asyncCurrentUser.invoke()?._id
        }
        adapter = OrderAdapter { orderId ->
            homeViewModel.handle(
                HomeViewAction.UpdateOrderStatus(
                    orderId,
                    shipperId!!,
                    UpdateStatusRequest(DELIVERING_STATUS)
                )
            )
        }
        views.rcyOrder.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun setupDeliveringLayout(quantity: Int) {
        views.layoutDelivery.visibility = View.VISIBLE
        views.orderQuantity.text = "Bạn có ${quantity} cần giao"
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
                initData()
                homeViewModel.handleRemoveAsyncUpdateOrderStatus()
            }

            is Fail -> {
                initData()
                homeViewModel.handleRemoveAsyncUpdateOrderStatus()
                showSnackbar(requireView(),"Đã có shipper khác nhận đơn",false,"Đã Hiểu"){
                }
            }

            else -> {}
        }
        when (it.asyncDelivering) {
            is Success -> {
                if (it.asyncDelivering.invoke()?.size!! > 0) {
                    setupDeliveringLayout(it.asyncDelivering.invoke()?.size!!)
                } else {
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
        when (it.asyncGetCurrentLocation) {
            is Success -> {
                it.asyncGetCurrentLocation.invoke()?.let { location ->
                    val currentLocation: String =
                        " " + location.address.road + ", " + location.address.quarter + ", " + location.address.suburb
                    setupAppBar(currentLocation)
                }
            }

            is Fail -> {
                setupAppBar(" Đang tải...")
            }

            else -> {}
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
    }

}