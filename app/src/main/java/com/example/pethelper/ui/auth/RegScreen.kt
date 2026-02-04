package com.example.pethelper.ui

import android.app.DatePickerDialog
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.ui.AppViewModelProvider
import com.example.pethelper.ui.orders.OrderDetails
import com.example.pethelper.ui.orders.OrderDialogViewModel
import java.util.Calendar

@Composable
fun RegistrationScreen(
    onBack:() -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onClick:(RegDetails) -> Unit = viewModel::updateUiState,
    onSubmit:() -> Unit = viewModel::submitRegistration,
    goToMain:() -> Unit = {}
    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState)

    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
        }
        Text(
            text = "Регистрация",
            modifier = Modifier.padding(bottom = 32.dp) // добавить шрифт
        )

        OutlinedTextField(
            value = uiState.details.firstName,
            onValueChange = { onClick(uiState.details.copy(firstName = it)) },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.details.lastName,
            onValueChange = { onClick(uiState.details.copy(lastName = it)) },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.details.email,
            onValueChange = { onClick(uiState.details.copy(email = it)) },
            label = { Text("Электронная почта") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.details.password,
            onValueChange = { onClick(uiState.details.copy(password = it)) },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.details.phone,
            onValueChange = { onClick(uiState.details.copy(phone = it)) },
            label = { Text("Номер телефона") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            )
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.details.address,
            onValueChange = { onClick(uiState.details.copy(address = it)) },
            label = { Text("Адрес") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        val calendar = Calendar.getInstance()
        val context = LocalContext.current

        OutlinedTextField(
            value = uiState.details.birthDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата рождения") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            onClick(uiState.details.copy(birthDate = "%02d.%02d.%d".format(day, month + 1, year)))
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
        )

        Spacer(Modifier.height(16.dp))

        val roles = listOf("Я работник", "Я хозяин")
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedTextField(
                value = roles[uiState.details.role],
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
                            if (it == roles[0]) {
                                onClick(uiState.details.copy(role = 0))
                            } else {
                                onClick(uiState.details.copy(role = 1))
                            }
                            expanded = false
                        })
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                onSubmit()
            },
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
        if (uiState.success) {
            goToMain()
        }
        else {
            Text(uiState.error)
        }
    }
}

