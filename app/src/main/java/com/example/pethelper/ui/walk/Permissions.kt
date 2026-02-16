package com.example.pethelper.ui.walk

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pethelper.Constants

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
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(brush = Constants.GRADIENT_BRUSH),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center){
                        Text("Разрешите доступ к геолокации",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF690005),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                            )
                        Spacer(Modifier.height(20.dp))
                        Button(onClick = {
                            foregroundLauncher.launch(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .padding(start = 40.dp, end = 40.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF690005),
                                contentColor = Color.White)
                        ){
                            Text("Разрешить",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                }
            }

            2 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = Constants.GRADIENT_BRUSH),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Для отслеживания выгула в фоне\n" +
                                "нужно разрешить доступ всегда",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF690005),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = {
                        openBackgroundLocationSettings(activity)
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(start = 40.dp, end = 40.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF690005),
                            contentColor = Color.White)
                    ) {
                        Text("Открыть настройки",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = {
                        navController!!.popBackStack()
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(start = 40.dp, end = 40.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF690005),
                            contentColor = Color.White)
                    ) {
                        Text("Готово",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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

