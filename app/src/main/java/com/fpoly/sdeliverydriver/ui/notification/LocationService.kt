package com.fpoly.sdeliverydriver.ui.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import android.app.PendingIntent
import android.content.Context
import android.os.PowerManager
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.data.model.User
import com.fpoly.sdeliverydriver.data.model.UserLocation
import com.fpoly.sdeliverydriver.ui.main.MainActivity
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.collection_user_locations

class LocationService : Service() {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        startForegroundServiceNotification()
        acquireWakeLock()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: called.")
        requestLocationUpdates()
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "LocationServiceChannel"
            val descriptionText = "Location Service Channel"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(PolyApplication.CHANNEL_ID, name, importance)
                .apply {
                    description = descriptionText
                }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startForegroundServiceNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, PolyApplication.CHANNEL_ID)
            .setContentTitle("Have a good day")
            .setContentText("Sdelivery driver is running in background")
            .setSmallIcon(R.drawable.address)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocationService::WakeLock")
        wakeLock?.acquire()
    }

    private fun requestLocationUpdates() {
        val mLocationRequestHighAccuracy = LocationRequest()
        mLocationRequestHighAccuracy.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequestHighAccuracy.interval = UPDATE_INTERVAL
        mLocationRequestHighAccuracy.fastestInterval = FASTEST_INTERVAL

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: stopping the location service.")
            stopSelf()
            return
        }

        mFusedLocationClient!!.requestLocationUpdates(mLocationRequestHighAccuracy, locationCallback, Looper.myLooper())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "onLocationResult: got location result.")
            val location = locationResult.lastLocation
            if (location != null) {
                val user: User? = (applicationContext as PolyApplication).currentUser
                val geoPoint = GeoPoint(location.latitude, location.longitude)
                val userLocation = UserLocation(user, geoPoint, null)
                saveUserLocation(userLocation)
            }
        }
    }

    private fun saveUserLocation(userLocation: UserLocation) {
        try {
            val locationRef = FirebaseFirestore.getInstance()
                .collection(collection_user_locations)
                .document(userLocation.user?._id.toString())
            locationRef.set(userLocation).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        TAG,
                        """onComplete: 
inserted user location into database.
 latitude: """ + userLocation.geoPoint
                            ?.getLatitude() +
                                "\n longitude: " + userLocation.geoPoint?.getLongitude()
                    )
                }
            }
        } catch (e: NullPointerException) {
            Log.e(TAG, "saveUserLocation: User instance is null, stopping location service.")
            Log.e(TAG, "saveUserLocation: NullPointerException: " + e.message)
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseWakeLock()
    }

    private fun releaseWakeLock() {
        wakeLock?.release()
    }

    companion object {
        private const val TAG = "LocationService"
        private const val UPDATE_INTERVAL = (4 * 1000 /* 4 secs */).toLong()
        private const val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    }
}

