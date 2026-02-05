package com.example.pethelper.ui.service

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.pethelper.data.rtdb.RealtimeOrderRepository
import com.example.pethelper.R

object c {
    const val NOTIFICATION_ID = 1001
}

class WalkTrackingService : Service() {

    private lateinit var locationManager: LocationManager
    private lateinit var rtdbRepo: RealtimeOrderRepository

    private var orderId: String = ""

    // listener GPS
    private val gpsListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {

            Log.d(
                "WalkService",
                "GPS: ${location.latitude}, ${location.longitude}, acc=${location.accuracy}"
            )

            // фильтр плохих координат
            if (location.accuracy > 30f) return

            rtdbRepo.updateLocation(
                orderId,
                location.latitude,
                location.longitude
            )
        }

        override fun onProviderEnabled(provider: String) {
            Log.d("WalkService", "GPS enabled")
        }

        override fun onProviderDisabled(provider: String) {
            Log.d("WalkService", "GPS disabled")
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("WalkService", "onCreate")

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        rtdbRepo = RealtimeOrderRepository()

        startForeground(
            c.NOTIFICATION_ID,
            createNotification()
        )
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        when (intent?.action) {

            WalkServiceActions.START -> {

                orderId = intent.getStringExtra("orderId")
                    ?: return START_NOT_STICKY

                Log.d("WalkService", "START orderId=$orderId")

                startLocationUpdates()
            }

            WalkServiceActions.STOP -> {

                Log.d("WalkService", "STOP")

                stopLocationUpdates()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null


    // ================= GPS =================

    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return


        // Проверяем что GPS включён
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Log.e("WalkService", "GPS is disabled")
            return
        }


        // Запуск GPS
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000,   // каждые 3 сек
            2f,     // 2 метра
            gpsListener
        )

        Log.d("WalkService", "GPS tracking started")
    }


    private fun stopLocationUpdates() {

        try {
            locationManager.removeUpdates(gpsListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.d("WalkService", "GPS tracking stopped")
    }


    // ================= Notification =================

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


object WalkServiceActions {

    const val START = "ACTION_START_WALK"
    const val STOP = "ACTION_STOP_WALK"
}