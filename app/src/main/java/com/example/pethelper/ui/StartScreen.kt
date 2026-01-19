package com.example.pethelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import ads_mobile_sdk.h4
import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.pethelper.data.fireBaseEntities.FUser
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

@Composable
fun MainScreen(auth: FirebaseAuth, ifAuth:() -> Unit, ifNoAuth:() -> Unit) {
    if (auth.currentUser != null) {
        ifAuth()
    }
    else {
        ifNoAuth()
    }
}

@Composable
fun AuthTrue(onCreateOrder: () -> Unit, onLogout:() -> Unit, onBack:() ->Unit, onAccount:() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "PetHelper",
            )

            IconButton(onClick = { onAccount() }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Аккаунт")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("")
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onCreateOrder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать заказ")
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                onLogout()
                onBack()
                      },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти")
        }
    }
}

@Composable
fun AuthFalse(onLogin: () -> Unit, onReg: () -> Unit, onBack:() ->Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Зарегистрируйтесь, или войдите в аккаунт"
        )
        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
        Button(
            onClick = onReg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }
    }
}


