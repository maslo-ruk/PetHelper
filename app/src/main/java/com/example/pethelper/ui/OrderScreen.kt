package com.example.pethelper.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions


@Composable
fun OrderRequestScreen() {
    var dateTime by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }

    var showDateDialog by remember { mutableStateOf(false) }
    var showDistrictDropdown by remember { mutableStateOf(false) }

    val moscowDistricts = listOf(
        "ЦАО (Центральный)",
        "САО (Северный)",
        "СВАО (Северо-Восточный)",
        "ВАО (Восточный)",
        "ЮВАО (Юго-Восточный)",
        "ЮАО (Южный)",
        "ЮЗАО (Юго-Западный)",
        "ЗАО (Западный)",
        "СЗАО (Северо-Западный)",
        "ЗелАО (Зеленоградский)",
        "ТиНАО (Троицкий и Новомосковский)",
        "Арбат",
        "Басманный",
        "Замоскворечье",
        "Красносельский",
        "Мещанский",
        "Пресненский",
        "Таганский",
        "Тверской",
        "Хамовники",
        "Якиманка",
        "Аэропорт",
        "Беговой",
        "Бескудниковский",
        "Войковский",
        "Головинский",
        "Дмитровский",
        "Коптево",
        "Левобережный",
        "Молжаниновский",
        "Савёловский",
        "Сокол",
        "Тимирязевский",
        "Ховрино",
        "Хорошёвский"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Форма заказа",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Дата и время с диалогом
        OutlinedTextField(
            value = dateTime,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDateDialog = true },
            label = { Text("Дата и время") },
            placeholder = { Text("Нажмите для выбора даты") },
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Выбрать дату")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Район Москвы с выпадающим списком
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedDistrict,
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDistrictDropdown = true },
                label = { Text("Район Москвы") },
                placeholder = { Text("Выберите район") },
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Выбрать район")
                }
            )
        }

        // Выпадающий список районов
        DropdownMenu(
            expanded = showDistrictDropdown,
            onDismissRequest = { showDistrictDropdown = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            moscowDistricts.forEach { district ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = district,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    },
                    onClick = {
                        selectedDistrict = district
                        showDistrictDropdown = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Примечания
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            label = { Text("Примечания") },
            placeholder = { Text("Порода, особенности животного, дополнительные пожелания...") },
            singleLine = false,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Цена
        OutlinedTextField(
            value = price,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    price = newValue
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Цена (руб.)") },
            placeholder = { Text("1000") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text("₽") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка отправки
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            enabled = dateTime.isNotEmpty() && selectedDistrict.isNotEmpty() && price.isNotEmpty()
        ) {
            Text("Создать заказ")
        }
    }

    // Диалог выбора даты
    if (showDateDialog) {
        AlertDialog(
            onDismissRequest = { showDateDialog = false },
            title = { Text("Выберите дату и время") },
            text = {
                Column {
                    // Простой выбор даты через текстовые поля
                    // В реальном приложении здесь будет DatePicker и TimePicker
                    Text("Дата будет реализована через DatePicker")
                    Spacer(modifier = Modifier.height(16.dp))

                    // Временное решение - кнопки для быстрого выбора
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                dateTime = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                                    .format(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                                showDateDialog = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Завтра 10:00")
                        }
                        Button(
                            onClick = {
                                dateTime = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                                    .format(Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000))
                                showDateDialog = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Послезавтра 10:00")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDateDialog = false }
                ) {
                    Text("Закрыть")
                }
            }
        )
    }
}

