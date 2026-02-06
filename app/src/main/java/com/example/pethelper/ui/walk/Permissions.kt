package com.example.pethelper.ui.walk

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun LocationPermissionScreen(
    navController: NavHostController? = null,
    onGranted: () -> Unit = {navController?.popBackStack()}
) {
    val context = LocalContext.current
    val activity = context as Activity

    var step by remember { mutableStateOf(1) }

    val foregroundLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            step = 2
        }
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (step) {

            1 -> {
                Text("Разрешите доступ к геолокации")
                Button (onClick = {
                    foregroundLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }) {
                    Text("Разрешить")
                }
            }

            2 -> {
                Text(
                    "Для отслеживания выгула в фоне\n" +
                            "нужно разрешить доступ всегда"
                )
                Button(onClick = {
                    openBackgroundLocationSettings(activity)
                }) {
                    Text("Открыть настройки")
                }
                Button(onClick = {
                    navController!!.popBackStack()
                }) {
                    Text("Готово")
                }
            }
        }
    }
}

fun openBackgroundLocationSettings(activity: Activity) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", activity.packageName, null)
    )
    activity.startActivity(intent)
}

