package com.example.pethelper.ui.account

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pethelper.Constants
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.network.NetworkConfig
import com.example.pethelper.ui.pets.PetCreateViewModel
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
fun AccountChange(onBack:()-> Unit = {},
                  viewModel: AccountChangeViewModel = viewModel(factory = AppViewModelProvider.Factory),
                  onValChange:(FUser) -> Unit = viewModel::updateUiState,
                  onSubmit:() -> Unit = viewModel::submit,
                  modifier: Modifier = Modifier,) {
    val alphaAnim = animateFloatAsState(1f, tween(800, easing = LinearOutSlowInEasing))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pets = viewModel.userManager.pets.collectAsState().value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Constants.GRADIENT_BRUSH)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Информация об аккаунте") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                painterResource(id = android.R.drawable.ic_media_previous),
                                contentDescription = "Назад"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UploadPhotoButton()
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = uiState.user.name,
                    onValueChange = { onValChange(uiState.user.copy(name = it)) },
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.user.surname,
                    onValueChange = { onValChange(uiState.user.copy(surname = it)) },
                    label = { Text("Фамилия") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.user.address,
                    onValueChange = { onValChange(uiState.user.copy(address = it)) },
                    label = { Text("Адрес") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = uiState.user.phoneNumber,
                    onValueChange = { onValChange(uiState.user.copy(phoneNumber = it)) },
                    label = { Text("Телефон") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = uiState.user.login,
                    onValueChange = { onValChange(uiState.user.copy(login = it)) },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
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
}


suspend fun uploadUserPhotoToServer(
    context: Context,
    uri: Uri,
    baseUrl: String = NetworkConfig.BASE_URL,
    client: OkHttpClient = OkHttpClient()): String {
    Log.e("PHOTO", "UploadPhoto composed")
    val bytes = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use {it.readBytes()} ?: error("Не могу прочитать")
    }
    val body = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart(name = "photo",
            filename = "image.png",
            body = bytes.toRequestBody("image/jpeg".toMediaType())).build()
    val request = Request.Builder().url("$baseUrl/upload").post(body).build()
    val respText = withContext(Dispatchers.IO) {
        client.newCall(request).execute().use() { resp ->
            val text = resp.body?.string().orEmpty()
            if (!resp.isSuccessful) error("Upload failed: ${resp.code}\n$text")
            text
        }
    }
    if (respText.isBlank()) error("Empty response (server returned empty body")
    val obj = JSONObject(respText)
    val fileId = obj.optString("file_id").ifBlank { obj.optString("fileId") }
    require(fileId.isNotBlank() && fileId != "null") {"Bad response: $respText"}
    return fileId
}

@Composable
fun UploadPhotoButton(viewModel: AccountChangeViewModel = viewModel(factory = AppViewModelProvider.Factory),
                      onUpdate: (FUser) -> Unit = viewModel::updateUiState) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var uploading by remember { mutableStateOf(false) }
    var lastFileId by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                val fileId = uploadUserPhotoToServer(context, uri, client = httpClient)
                lastFileId = fileId
                onUpdate(uiState.user.copy(photoId = fileId))
            // сохранение в бд
            }
            catch (e: Exception) {
                error = e.message }
            finally {
                uploading = false
            }
        }
    }

    Button(
        onClick = { pickImageLauncher.launch(arrayOf("image/*")) },
        enabled = !uploading,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(color = 0xFF690005),
            contentColor = Color.White)
    ) {
        Text(if (uploading) "загружаю" else "Выбрать фото")
        lastFileId?.let { Text("fileId: $it") }
        error?.let { Text("Ошибка: $it")}
    }
}
