package com.example.pethelper.ui.service
//
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.pethelper.data.rtdb.RealtimeOrderRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pethelper.R


object c{
    const val NOTIFICATION_ID = 1001
}

class WalkTrackingService: Service(){
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var rtdbRepo: RealtimeOrderRepository

    private var orderId: String = ""

    override fun onCreate() {
        super.onCreate()
        Log.d("WalkService", "onCreate start")

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        rtdbRepo = RealtimeOrderRepository()

        startForeground(
            c.NOTIFICATION_ID,
            createNotification()
        )
        Log.d("WalkService", "onCreate success")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        orderId = intent?.getStringExtra("orderId") ?: return START_NOT_STICKY
        Log.d("WalkService", "onStartCommand orderId=${intent?.getStringExtra("orderId")}")
        startLocationUpdates()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startLocationUpdates() {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000 // каждые 5 секунд
        ).build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation ?: return
            Log.d("WalkService", "Location result = $location")
            rtdbRepo.updateLocation(
                orderId = orderId,
                lat = location.latitude,
                lon = location.longitude
            )
        }
    }

    private fun createNotification(): Notification {
        val channelId = "walk_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Активный выгул",
                NotificationManager.IMPORTANCE_LOW
            )

            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Идёт выгул")
            .setContentText("Геолокация передаётся")
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .build()
    }
}