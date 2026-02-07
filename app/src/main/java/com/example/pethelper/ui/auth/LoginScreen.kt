package com.example.pethelper.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.ui.AppViewModelProvider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pethelper.Constants

@Composable
fun LoginScreen(onBack: () -> Unit,
                viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
                onSubmit:() -> Unit = viewModel::submitRegistration,
                onUpdate:(LoginDetails) -> Unit = viewModel::updateUiState,
                goToMain:() -> Unit = {}) {
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
                text = "Войти с паролем",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF690005),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = uiState.details.email,
                onValueChange = { onUpdate(uiState.details.copy(email = it)) },
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
                onValueChange = { onUpdate(uiState.details.copy(password = it)) },
                label = { Text("Пароль") },
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
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { onSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(color = 0xFF690005),
                    contentColor = Color.White
                )
            ) {
                Text("Войти",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold)
            }
            if (uiState.success) {
                goToMain()
            } else {
                Text(uiState.error)
            }
        }
    }
}