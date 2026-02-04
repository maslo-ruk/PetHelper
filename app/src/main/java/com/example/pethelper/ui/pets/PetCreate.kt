package com.example.pethelper.ui.pets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Update
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.ui.AppViewModelProvider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCreate(onBack: () -> Unit,
              modifier: Modifier= Modifier,
              viewModel: PetCreateViewModel = viewModel(factory = AppViewModelProvider.Factory),
              onUpdate: (FPet) -> Unit = viewModel::updateStateFlow,
              onSubmit: () -> Unit = viewModel::submitPet
 ) {
    /*я не разобаралась с классом животного, потом изменим*/
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    var petName by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавление питомца") },
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally, ) {
            /*Изображение питомца*/
            OutlinedButton(
                onClick = { /*диалог выбора изображения*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Добавить изображение")
            }
            Spacer(modifier= Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.details.name,
                onValueChange = { onUpdate(uiState.details.copy(name = it)) },
                label = { Text("Имя питомца") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            /*порода, сделала первую букву заглавной, чтобы в бд было легче искать всегда*/
            OutlinedTextField(
                value = uiState.details.breed,
                onValueChange = { input -> onUpdate(uiState.details.copy(breed = input.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()}))
                },
                label = { Text("Порода") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.details.age.toString(),
                onValueChange = { onUpdate(uiState.details.copy(age = it.toInt())) },
                label = { Text("Возраст") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.details.weight.toString(),
                onValueChange = { onUpdate(uiState.details.copy(weight = it.toInt())) },
                label = { Text("Вес (кг)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = uiState.details.description,
                onValueChange = { onUpdate(uiState.details.copy(description = it)) },
                label = { Text("Примечания по характеру, особенности") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(onClick = { onSubmit() }, modifier = Modifier
                .fillMaxWidth()) {
                Text("Сохранить")
            }
            if (uiState.isLoading) {
                Text("Загрузка...")
            }
            if (uiState.isSuccess) {
                onBack()
            }
        }
    }

}