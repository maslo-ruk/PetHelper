package com.example.pethelper.ui

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
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDialog(onClose: () -> Unit) {
    val context = LocalContext.current
    val districts = listOf(
        "ЦАО", "САО", "СВАО", "ВАО", "ЮВАО", "ЮАО", "ЮЗАО", "ЗАО", "СЗАО", "ЗелАО"
    )

    var selectedDistrict by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var showFields by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { showFields = true }

    Dialog(onDismissRequest = { onClose() }) {
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
                        Text("Имя: Иван", fontSize = 18.sp)
                        Text("Фамилия: Петров", fontSize = 18.sp)
                        Text("Телефон: +7 900 000 00 00", fontSize = 18.sp)
                    }
                }

                // Дата
                AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Дата") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                val calendar = Calendar.getInstance()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        date = "$dayOfMonth/${month + 1}/$year"
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
                        value = time,
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
                                        time = String.format("%02d:%02d", hourOfDay, minute)
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

                // Районы
                AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
                    ExposedDropdownMenuBox(
                        expanded = selectedDistrict.isNotEmpty(),
                        onExpandedChange = { selectedDistrict = if (it) selectedDistrict else "" }
                    ) {
                        OutlinedTextField(
                            value = selectedDistrict,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Район Москвы") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = selectedDistrict.isNotEmpty()
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = selectedDistrict.isNotEmpty(),
                            onDismissRequest = { selectedDistrict = "" }
                        ) {
                            districts.forEach { district ->
                                DropdownMenuItem(
                                    text = { Text(district) },
                                    onClick = {/*TODO*/}
                                )
                            }
                            }
                        }
                    }
                }

                // Цена
                AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { if (it.all { char -> char.isDigit() }) price = it },
                        label = { Text("Цена") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                // Примечания
                AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        label = { Text("Примечания по заказу") },
                        maxLines = 10
                    )
                }

                // Кнопка "Создать заказ" (ничего не делает)
                AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
                    Button(
                        onClick = { /* ничего не делаем */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Создать заказ")
                    }
                }
            }
        }
    }
