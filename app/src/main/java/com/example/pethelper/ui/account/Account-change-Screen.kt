package com.example.pethelper.ui.account

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.R
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.ui.AppViewModelProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountChange(onBack:()-> Unit = {},
                  viewModel: AccountChangeViewModel = viewModel(factory = AppViewModelProvider.Factory),
                  onValChange:(FUser) -> Unit = viewModel::updateUiState,
                  onSubmit:() -> Unit = viewModel::submit,
                  modifier: Modifier = Modifier,) {
    val alphaAnim = animateFloatAsState(1f, tween(800, easing = LinearOutSlowInEasing))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pets = viewModel.userManager.pets.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Информация об аккаунте") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(id = android.R.drawable.ic_media_previous), contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier=Modifier, contentAlignment = Alignment.BottomEnd) {
                Image(painter = painterResource(R.drawable.cot), contentDescription = "Аватар",
                    Modifier
                        .size(120.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .clip(CircleShape))
                IconButton(onClick = {/*TODO*/},
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .size(36.dp)) {
                            Icon(imageVector = Icons.Default.Edit,
                                contentDescription = "Изменить аватар",
                                tint = MaterialTheme.colorScheme.secondary)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = uiState.user.name,
                onValueChange = {onValChange(uiState.user.copy(name = it))},
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(value = uiState.user.surname,
                onValueChange = {onValChange(uiState.user.copy(surname = it))},
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.user.address,
                onValueChange = {onValChange(uiState.user.copy(address = it))},
                label = { Text("Адрес") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = uiState.user.phoneNumber,
                onValueChange = {onValChange(uiState.user.copy(phoneNumber = it))},
                label = { Text("Телефон") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier= Modifier.height(12.dp))
            var showDatePicker by remember { mutableStateOf(false) }
            val datePickerState = rememberDatePickerState()
//            OutlinedTextField(
//                value = uiState.user.birthDate.let {
//                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
//                        .format(Date(it))
//                } ?: "",
//                onValueChange = {},
//                label = { Text("Дата рождения") },
//                readOnly = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { showDatePicker = true }
//            )
//            if (showDatePicker) {
//                DatePickerDialog(
//                    onDismissRequest = { showDatePicker = false },
//                    confirmButton = {
//                        TextButton(onClick = {
//                            showDatePicker = false /*тут нужно добавить изменение даты*/
//                        }) {
//                            Text("ОК")
//                        }
//                    },
//                    dismissButton = {
//                        TextButton(onClick = { showDatePicker = false }) {
//                            Text("Отмена")
//                        }
//                    }
//                ) {
//                    DatePicker(state = datePickerState)
//                }
//            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = { onSubmit() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Готово")
            }
            if (uiState.isLoading) {
                Text("Загрузка...")
            }
            if (uiState.success) {
                onBack()
            }
        }
        }


}