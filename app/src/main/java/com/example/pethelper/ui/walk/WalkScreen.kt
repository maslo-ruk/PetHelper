package com.example.pethelper.ui.walk

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.ui.AppViewModelProvider

@Composable
fun WalkScreen(
    viewModel: WalkViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onClick1:(c: Context, orderId: String)->Unit = viewModel::onWalkStarted,
    onClick2:(c: Context, orderId: String)->Unit = viewModel::onWalkFinished,
    orderId:String = "ORDERBORDER1",
    modifier: Modifier = Modifier
) {
    val location by viewModel.location.collectAsState()

    val context = LocalContext.current

    var hasPermission = hasBackgroundLocation(context)

    if (hasPermission) {
        var is_pressed by remember { mutableStateOf(false) }

        Column {
            Button(
                onClick = {
                    onClick1(context, orderId)
                    is_pressed = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Начать геолокацию")
            }

            Button(
                onClick = {
                    onClick2(context, orderId)
                    is_pressed = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Закончить геолокацию")
            }

            if (is_pressed) {
                if (location == null) {
                    Text("Ожидание геолокации…")
                } else {
                    val (lat, lng) = location!!
                    Text("Широта: $lat\nДолгота: $lng")
                }
            }
        }
    } else {
        LocationPermissionScreen(
            onGranted = {
                hasPermission = true
            }
        )
    }
}

fun hasBackgroundLocation(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    } else true
}