//package com.example.pethelper.ui.service
////
//import android.app.Service
//import android.content.Intent
//import android.os.IBinder
//import android.os.Looper
//import com.example.pethelper.data.rtdb.RealtimeOrderRepository
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.Priority
//
//
//class WalkTrackingService: Service(){
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var rtdbRepo: RealtimeOrderRepository
//
//    private var orderId: String = ""
//
//    override fun onCreate() {
//        super.onCreate()
//
//        fusedLocationClient =
//            LocationServices.getFusedLocationProviderClient(this)
//
//        rtdbRepo = RealtimeOrderRepository()
//
//        startForeground(
//            NOTIFICATION_ID,
//            createNotification()
//        )
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        orderId = intent?.getStringExtra("orderId") ?: return START_NOT_STICKY
//
//        startLocationUpdates()
//        return START_STICKY
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    private fun startLocationUpdates() {
//        val request = LocationRequest.Builder(
//            Priority.PRIORITY_HIGH_ACCURACY,
//            5000 // каждые 5 секунд
//        ).build()
//
//        fusedLocationClient.requestLocationUpdates(
//            request,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//    }
//
//    private val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(result: LocationResult) {
//            val location = result.lastLocation ?: return
//
//            rtdbRepo.updateLocation(
//                orderId = orderId,
//                lat = location.latitude,
//                lon = location.longitude
//            )
//        }
//    }
//}