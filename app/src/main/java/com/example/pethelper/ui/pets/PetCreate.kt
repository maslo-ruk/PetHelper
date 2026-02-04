package com.example.pethelper.ui.pets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.pethelper.network.NetworkConfig
import com.example.pethelper.ui.AppViewModelProvider
import android.content.Context
import androidx.compose.animation.core.spring
import androidx.compose.material3.Button
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit


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
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally, ) {
            Spacer(modifier= Modifier.height(12.dp))

            UploadPhotoButton()

            Spacer(modifier = Modifier.height(12.dp))

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

suspend fun uploadPhotoToServer(
    context: Context,
    uri: Uri,
    baseUrl: String = NetworkConfig.BASE_URL,
    client: OkHttpClient = OkHttpClient()): String {
    val bytes = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use {it.readBytes()} ?: error("Не могу прочитать")
    }
    val body = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart(name = "photo",
            filename = "image.png",
            body = bytes.toRequestBody("image/jpeg".toMediaType())).build()
    val request = Request.Builder().url("$baseUrl/upload").post(body).build()
    val resp = withContext(Dispatchers.IO) {
        client.newCall(request).execute()
    }
    if (!resp.isSuccessful) {
        error("Upload failed")
    }
    val json = resp.body?.string() ?: error("Empty response")
    return JSONObject(json).getString("fileId")
}


@Composable
fun UploadPhotoButton() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var uploading by remember { mutableStateOf(false) }
    var lastFileId by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .callTimeout(90, TimeUnit.SECONDS).build()
    val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
        uri: Uri? -> if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            try {
                error = null
                uploading = true
                val fileId = uploadPhotoToServer(context, uri, client = httpClient)
                lastFileId = fileId} catch (e: Exception) {
                    error = e.message } finally {
                        uploading = false
                    }
            }
        }
    Button(
        onClick = { pickImageLauncher.launch(arrayOf("image/*")) },
        enabled = !uploading) {
        Text(if (uploading) "загружаю" else "Выбрать фото")
        lastFileId?.let { Text("fileId: $it") }
        error?.let { Text("Ошибка: $it")}
    }
}