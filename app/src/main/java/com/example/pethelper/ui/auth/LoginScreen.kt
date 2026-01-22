package com.example.pethelper.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.ui.AppViewModelProvider

@Composable
fun LoginScreen(onBack: () -> Unit,
                viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
                onSubmit:() -> Unit = viewModel::submitRegistration,
                onUpdate:(LoginDetails) -> Unit = viewModel::updateUiState,
                goToMain:() -> Unit = {}) {
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
            text = "Вход",
            modifier = Modifier.padding(vertical = 24.dp)
        )

        OutlinedTextField(
            value = uiState.details.email,
            onValueChange = { onUpdate(uiState.details.copy(email = it)) },
            label = { Text("Электронная почта") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.details.password,
            onValueChange = { onUpdate(uiState.details.copy(password = it)) },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { onSubmit() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
        if (uiState.success) {
            goToMain()
        }
        else {
            Text(uiState.error)
        }
    }
}