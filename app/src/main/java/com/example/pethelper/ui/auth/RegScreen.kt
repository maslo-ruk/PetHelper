package com.example.pethelper.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.Constants
import com.example.pethelper.ui.AppViewModelProvider
import com.example.pethelper.ui.auth.RegDetails
import com.example.pethelper.ui.auth.RegViewModel
import com.example.pethelper.ui.orders.OrderDetails
import com.example.pethelper.ui.orders.OrderDialogViewModel
import java.util.Calendar
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Person
@Composable
fun RegistrationScreen(
    onBack:() -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegViewModel= viewModel(factory = AppViewModelProvider.Factory),
    onClick:(RegDetails) -> Unit = viewModel::updateUiState,
    onSubmit:() -> Unit = viewModel::submitRegistration,
    goToMain:() -> Unit = {}
    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Constants.GRADIENT_BRUSH)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState)

        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Назад", tint = Color(0xFF690005))
            }
            Text(
                text = "Регистрация",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF690005),
                textAlign = TextAlign.Center// добавить шрифт
            )

            OutlinedTextField(
                value = uiState.details.firstName,
                onValueChange = { onClick(uiState.details.copy(firstName = it)) },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.details.lastName,
                onValueChange = { onClick(uiState.details.copy(lastName = it)) },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PermIdentity,
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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.details.email,
                onValueChange = { onClick(uiState.details.copy(email = it)) },
                label = { Text("Электронная почта") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.details.password,
                onValueChange = { onClick(uiState.details.copy(password = it)) },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.details.phone,
                onValueChange = { onClick(uiState.details.copy(phone = it)) },
                label = { Text("Номер телефона") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.details.address,
                onValueChange = { onClick(uiState.details.copy(address = it)) },
                label = { Text("Адрес") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Home,
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

            Spacer(Modifier.height(16.dp))

            val calendar = Calendar.getInstance()
            val context = LocalContext.current

            OutlinedTextField(
                value = uiState.details.birthDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Дата рождения") },
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF690005)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                onClick(
                                    uiState.details.copy(
                                        birthDate = "%02d.%02d.%d".format(
                                            day,
                                            month + 1,
                                            year
                                        )
                                    )
                                )
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
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

            Spacer(Modifier.height(16.dp))

            val roles = listOf("Я работник", "Я хозяин")
            var expanded by remember { mutableStateOf(false) }

            Box {
                OutlinedTextField(
                    value = roles[uiState.details.role],
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Выберите роль") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown, contentDescription = null,
                            Modifier.clickable { expanded = true })
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.SupervisorAccount,
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

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    roles.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(color = 0xFF690005),
                    contentColor = Color.White
                )
            ) {
                Text("Регистрация",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold)
            }

        if (uiState.waitingForEmailVerification) {
            Spacer(Modifier.height(16.dp))
            Text("Мы отправили письмо на ${uiState.details.email}. Подтвердите почту по ссылке в письме.")

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { viewModel.checkEmailVerifiedAndFinish() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Я подтвердил почту")
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Отправить письмо ещё раз",
                modifier = Modifier.clickable { viewModel.resendVerificationEmail() }
            )
        }

        Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Уже есть аккаунт?",
                    color = Color(0xFF000000))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Войти",
                    modifier = Modifier.clickable { onLoginClick() },
                    color = Color(0xFF690005)
                )
            }
            if (uiState.success) {
                goToMain()
            } else {
                Text(uiState.error)
            }
        }
    }
}

