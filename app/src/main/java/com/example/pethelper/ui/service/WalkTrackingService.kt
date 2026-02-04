//package com.example.pethelper.ui.service
//
//import android.app.Service
//import android.content.Intent
//import android.os.IBinder
//import android.os.Looper
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.Priority
//
//
//class WalkTrackingService: Service(){
//    private lateinit var locationClient: FusedLocationProviderClient
//    private lateinit var locationCallback: LocationCallback
//
//    override fun onCreate() {
//        super.onCreate()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        locationClient.removeLocationUpdates(locationCallback)
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    private fun startLocationUpdates() {
//        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5_000L).build()
//
//
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(result: LocationResult) {
//                val location = result.lastLocation ?: return
//                sendLocation(location)
//            }
//        }
//
//        locationClient.requestLocationUpdates(
//            request,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//
//    }
//}