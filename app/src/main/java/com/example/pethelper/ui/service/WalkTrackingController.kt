package com.example.pethelper.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.pethelper.ui.service.WalkServiceActions
import com.example.pethelper.ui.service.WalkTrackingService

object WalkServiceController {

    fun startWalkService(
        context: Context,
        orderId: String
    ) {
        val intent = Intent(context, WalkTrackingService::class.java).apply {
            action = WalkServiceActions.START
            putExtra("orderId", orderId)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun stopWalkService(context: Context) {

        val intent = Intent(context, WalkTrackingService::class.java).apply {
            action = WalkServiceActions.STOP
        }
        ContextCompat.startForegroundService(context, intent)
    }
}