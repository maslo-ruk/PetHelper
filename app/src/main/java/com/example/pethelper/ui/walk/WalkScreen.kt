package com.example.pethelper.ui.walk

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView

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
        val is_pressed by viewModel.locating.collectAsStateWithLifecycle()

        Column {
            Button(
                onClick = {
                    onClick1(context, orderId)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Начать геолокацию")
            }

            Button(
                onClick = {
                    onClick2(context, orderId)
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
                    val mapView = remember {
                        com.yandex.mapkit.mapview.MapView(context).apply {
                            map.move(
                                com.yandex.mapkit.map.CameraPosition(
                                    com.yandex.mapkit.geometry.Point(location!!.first, location!!.second),
                                    16f,  // zoom
                                    0f,   // azimuth
                                    0f    // tilt
                                )
                            )
                        }
                    }

                    //маркер
                    val placemarkRef = remember {mutableStateOf<PlacemarkMapObject?>(null)}

                    // прокидываем lifecycle в mapkit
                    val lifecycle = LocalLifecycleOwner.current.lifecycle
                    DisposableEffect(lifecycle) {
                        val observer = LifecycleEventObserver { _, event ->
                            when(event) {
                                Lifecycle.Event.ON_START -> {
                                    MapKitFactory.getInstance().onStart()
                                    mapView.onStart()
                                }
                                Lifecycle.Event.ON_STOP -> {
                                    mapView.onStop()
                                    MapKitFactory.getInstance().onStop()
                                }
                                else -> Unit
                            }
                        }
                        lifecycle.addObserver(observer)
                        onDispose { lifecycle.removeObserver(observer) }
                    }

                    LaunchedEffect(location) {
                        val point = Point(location!!.first, location!!.second)

                        val mapObjects = mapView.map.mapObjects
                        val placemark = placemarkRef.value ?: mapObjects.addPlacemark(point).also {
                            placemarkRef.value = it
                        }
                        placemark.geometry = point

                        // Если хочешь, чтобы камера всегда следовала за человеком:
                        mapView.map.move(
                            com.yandex.mapkit.map.CameraPosition(point, 16f, 0f, 0f)
                        )
                    }

                    AndroidView(
                        factory = { mapView },
                        modifier = modifier.fillMaxSize()
                    )
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