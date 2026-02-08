package com.example.pethelper.ui.orders

import ads_mobile_sdk.ui
import androidx.compose.runtime.Composable


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Pets
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.Constants
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.ui.AppViewModelProvider
import java.util.*
import kotlin.math.exp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDialog(modifier:Modifier = Modifier,
                viewModel: OrderDialogViewModel = viewModel(factory = AppViewModelProvider.Factory),
                onClose:()->Unit) {
    var showFields by remember { mutableStateOf(true) }
    val curUser:FUser? = viewModel.userManager.currentUser.collectAsState().value?.user
    val pets = viewModel.userManager.pets.collectAsState().value
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
                .fillMaxWidth()
                .background(brush = Constants.GRADIENT_BRUSH)
                .border(
                    width = 2.dp,
                    color = Color(0xFF690005),
                    shape = RoundedCornerShape(8.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Заголовок с крестиком
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Создать заказ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF690005),
                    modifier = Modifier
                        .padding(end = 20.dp)
                )
                IconButton(onClick = { onClose() }) {
                    Icon(Icons.Default.Close,
                        contentDescription = "Закрыть окно",
                        tint = Color(0xFF690005)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Неизменяемая информация
            AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(),
                    ) {
                    Text(curUser!!.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF690005)
                    )
                    Text(curUser.surname,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF690005)
                    )
                    Text("Телефон: ${curUser.phoneNumber}",
                        fontSize = 18.sp,
                        color = Color(0xFF690005)
                    )
                }
            }

            OrderInputs(
                modifier = Modifier,
                onClick = viewModel::updateUiState,
                onSave = viewModel::submitOrder,
                onBack = onClose,
                uiState = uiState,
                pets = pets
                )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderInputs(modifier:Modifier = Modifier,
                onClick:(OrderDetails) -> Unit,
                onSave:() -> Unit,
                onBack:() -> Unit,
                uiState: OrderUiState,
                pets:List<FPet>
                )
{
    val context = LocalContext.current
    var showFields by remember { mutableStateOf(true) }

    var expanded1 by remember { mutableStateOf(false) }
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        AnimatedVisibility(
            visible = showFields,
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            ExposedDropdownMenuBox(
                expanded = expanded1,
                onExpandedChange = {
                    expanded1 = !expanded1   // ← важно!
                }
            ) {

                OutlinedTextField(
                    value = uiState.details.pet.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Выберите питомца") },

                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded1)
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Pets,
                            contentDescription = null,
                            tint = Color(0xFF690005)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        // Цвет рамки
                        focusedBorderColor = Color(0xFF690005),
                        unfocusedBorderColor = Color(0xFF690005),

                        // Цвет текста
                        focusedTextColor = Color(0xFF690005),
                        unfocusedTextColor = Color(0xFF690005),

                        // Цвет label
                        focusedLabelColor = Color(0xFF690005),
                        unfocusedLabelColor = Color(0xFF690005),

                        // Цвет курсора
                        cursorColor = Color(0xFF690005)
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded1,
                    onDismissRequest = {
                        expanded1 = false
                    }
                ) {

                    pets.forEach { pet ->

                        DropdownMenuItem(
                            text = { Text(pet.name) },

                            onClick = {
                                onClick(uiState.details.copy(pet = pet))
                                expanded1 = false   // ← закрываем меню
                            }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
            OutlinedTextField(
                value = uiState.details.date,
                onValueChange = {/*onClick(viewModel._uiState.value.details.copy(date = it))*/},
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFF690005)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    // Цвет рамки
                    focusedBorderColor = Color(0xFF690005),
                    unfocusedBorderColor = Color(0xFF690005),

                    // Цвет текста
                    focusedTextColor = Color(0xFF690005),
                    unfocusedTextColor = Color(0xFF690005),

                    // Цвет label
                    focusedLabelColor = Color(0xFF690005),
                    unfocusedLabelColor = Color(0xFF690005),

                    // Цвет курсора
                    cursorColor = Color(0xFF690005)
                ),
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
                    }
                }
            )
        }


        // Время
        AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
            OutlinedTextField(
                value = uiState.details.time,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF690005)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    // Цвет рамки
                    focusedBorderColor = Color(0xFF690005),
                    unfocusedBorderColor = Color(0xFF690005),

                    // Цвет текста
                    focusedTextColor = Color(0xFF690005),
                    unfocusedTextColor = Color(0xFF690005),

                    // Цвет label
                    focusedLabelColor = Color(0xFF690005),
                    unfocusedLabelColor = Color(0xFF690005),

                    // Цвет курсора
                    cursorColor = Color(0xFF690005)
                ),
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
                    }
                }
            )
        }
        AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
            OutlinedTextField(
                value = uiState.details.address,
                onValueChange = {onClick(uiState.details.copy(address = it))},
                label = {Text("Адрес выполнения")},
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        tint = Color(0xFF690005)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    // Цвет рамки
                    focusedBorderColor = Color(0xFF690005),
                    unfocusedBorderColor = Color(0xFF690005),

                    // Цвет текста
                    focusedTextColor = Color(0xFF690005),
                    unfocusedTextColor = Color(0xFF690005),

                    // Цвет label
                    focusedLabelColor = Color(0xFF690005),
                    unfocusedLabelColor = Color(0xFF690005),

                    // Цвет курсора
                    cursorColor = Color(0xFF690005)
                )
            )
        }

        // Цена
        AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
            OutlinedTextField(
                value = uiState.details.price.toString(),
                onValueChange = {new ->
                    val text = new
                    val amount = text.toIntOrNull() ?: 0
                    onClick(uiState.details.copy(price = amount))}, // фикс вылета
                label = { Text("Цена") },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = Color(0xFF690005)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    // Цвет рамки
                    focusedBorderColor = Color(0xFF690005),
                    unfocusedBorderColor = Color(0xFF690005),

                    // Цвет текста
                    focusedTextColor = Color(0xFF690005),
                    unfocusedTextColor = Color(0xFF690005),

                    // Цвет label
                    focusedLabelColor = Color(0xFF690005),
                    unfocusedLabelColor = Color(0xFF690005),

                    // Цвет курсора
                    cursorColor = Color(0xFF690005)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // Примечания
        AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
            OutlinedTextField(
                value = uiState.details.notes,
                onValueChange = { onClick(uiState.details.copy(notes = it)) },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ModeComment,
                        contentDescription = null,
                        tint = Color(0xFF690005)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    // Цвет рамки
                    focusedBorderColor = Color(0xFF690005),
                    unfocusedBorderColor = Color(0xFF690005),

                    // Цвет текста
                    focusedTextColor = Color(0xFF690005),
                    unfocusedTextColor = Color(0xFF690005),

                    // Цвет label
                    focusedLabelColor = Color(0xFF690005),
                    unfocusedLabelColor = Color(0xFF690005),

                    // Цвет курсора
                    cursorColor = Color(0xFF690005)
                ),
                label = { Text("Комментарии к заказу") },
                maxLines = 10
            )
        }

        if (!uiState.isLoading) {
            // Кнопка "Создать заказ"
            AnimatedVisibility(visible = showFields, enter = fadeIn(), exit = fadeOut()) {
                Button(
                    onClick = { onSave() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .padding(bottom = 10.dp, start = 20.dp, end = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(color = 0xFF690005),
                        contentColor = Color.White
                    )
                ) {
                    Text("Создать заказ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            }
        }
        if (uiState.isLoading) {
            Text("Загрузка...")
        }
        if (uiState.error != "") {
            Text(uiState.error)
        }
        if (uiState.success) {
            onBack()
        }

    }

}