package com.example.pethelper.ui.orders

import androidx.compose.runtime.Composable


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.entities.User
import com.example.pethelper.ui.AppViewModelProvider
import java.util.*
import kotlin.math.exp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDialog(modifier:Modifier = Modifier,
                viewModel: OrderDialogViewModel = viewModel(factory = AppViewModelProvider.Factory),
                onClose:()->Unit,
                currentUser:User
) {


    var showFields by remember { mutableStateOf(true) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { showFields = true }
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Заголовок с крестиком
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Создать заказ", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { onClose() }) {
                    Icon(Icons.Default.Close, contentDescription = "Закрыть окно")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Неизменяемая информация
            AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(currentUser.name, fontSize = 18.sp)
                    Text(currentUser.surname, fontSize = 18.sp)
                    Text("Телефон: ${currentUser.phoneNumber}", fontSize = 18.sp)
                }
            }

            OrderInputs(
                modifier = Modifier,
                onClick = viewModel::updateUiState,
                onSave = viewModel::submitOrder,
                onClose = onClose,
                currentUser = currentUser,
                uiState = uiState
                )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderInputs(modifier:Modifier = Modifier,
                onClick:(OrderDetails) -> Unit, onSave:() -> Unit, onClose:() -> Unit,
                currentUser: User,
                uiState: OrderUiState)
{
    val context = LocalContext.current
    val districts = listOf(
        "ЦАО", "САО", "СВАО", "ВАО", "ЮВАО", "ЮАО", "ЮЗАО", "ЗАО", "СЗАО", "ЗелАО"
    )
    var showFields by remember { mutableStateOf(true) }
    AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
        OutlinedTextField(
            value = uiState.details.date,
            onValueChange = {/*onClick(viewModel._uiState.value.details.copy(date = it))*/},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Дата") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            onClick(uiState.details.copy(date = "$dayOfMonth/${month + 1}/$year"))
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Выбрать дату")
                }
            }
        )
    }

    // Время
    AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
        OutlinedTextField(
            value = uiState.details.time,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Время") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    val calendar = Calendar.getInstance()
                    TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            onClick(uiState.details.copy(
                                time = String.format("%02d:%02d", hourOfDay, minute))
                            )
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                }) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Выбрать время")
                }
            }
        )
    }
    var expanded by remember { mutableStateOf(false) }
    // Районы
    AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = uiState.details.address,
                onValueChange = {},
                readOnly = true,
                label = { Text("Район Москвы") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onClick(uiState.details.copy(address = "")) }
            ) {
                districts.forEach { district ->
                    DropdownMenuItem(
                        text = { Text(district) },
                        onClick = {onClick(uiState.details.copy(address = district))}
                    )
                }
            }
        }
    }


    // Цена
    AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
        OutlinedTextField(
            value = uiState.details.price.toString(),
            onValueChange = { onClick(uiState.details.copy(price=it.toInt())) },
            label = { Text("Цена") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }

    // Примечания
    AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
        OutlinedTextField(
            value = uiState.details.notes,
            onValueChange = { onClick(uiState.details.copy(notes = it)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            label = { Text("Примечания по заказу") },
            maxLines = 10
        )
    }

    // Кнопка "Создать заказ"
    AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать заказ")
        }
    }
}