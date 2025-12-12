package com.example.pethelper.ui

import ads_mobile_sdk.h4
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton

@Composable
fun StartScreen(modifier:Modifier = Modifier,
                userId:Int = 0) {
    var screen by remember {mutableStateOf("registration")}
    when (screen) {
        "registration" -> RegistrationScreen { screen = "login" }
        "login" -> LoginScreen { screen = "registration" }
        "main" -> MainScreen()
    }

}

@Composable
fun RegistrationScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {

        Text(
            text = "Регистрация",
            modifier = Modifier.padding(bottom = 32.dp) // добавить шрифт
        )

        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var role by remember { mutableStateOf("Я работник") }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("ФИО") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Электронная почта") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))

        val roles = listOf("Я работник", "Я рабочий")
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedTextField(
                value = role,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Выберите роль") },
                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown, contentDescription = null,
                        Modifier.clickable { expanded = true })
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach {
                    DropdownMenuItem(text = { Text(it) },
                        onClick = {
                        role = it
                        expanded = false
                    })
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Регистрация")
        }

        Spacer(Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Уже есть аккаунт?")
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Войти",
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}


@Composable
fun LoginScreen(onBack: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
        }

        Text(
            text = "Вход",
            modifier = Modifier.padding(vertical = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Электронная почта") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
    }
}

@Composable
fun MainScreen(onCreateOrder: () -> Unit = {}) {
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

            IconButton(onClick = { /* TODO: профиль */ }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Аккаунт")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCreateOrder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать заказ")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Пустое пространство под будущее
    }
}




