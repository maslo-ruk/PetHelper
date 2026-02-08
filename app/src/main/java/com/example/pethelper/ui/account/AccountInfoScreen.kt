package com.example.pethelper.ui.account

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.ui.AppViewModelProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.pethelper.Constants
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.R
import com.example.pethelper.network.NetworkConfig
import com.example.pethelper.ui.start.StartViewModel

@ExperimentalMaterial3Api
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onEditAccount: () -> Unit = {},
    onOpenAccountInfo: () -> Unit = {},
    onOpenPet: (petId: String) -> Unit = {},
    onAddPet: () -> Unit = {},
    onBack:() -> Unit = {},
    goToStart:() ->Unit = {},
    onLogout:() -> Unit = viewModel::logout
) {
    val alphaAnim = animateFloatAsState(1f, tween(800, easing = LinearOutSlowInEasing))
    val curUser:FUser? = viewModel.userManager.currentUser.collectAsState().value?.user
    val pets = viewModel.userManager.pets.collectAsState().value
    //val petUiState by viewModel.uiState.collectAsStateWithLifecycle()
    //val isLogged by viewModel.isLogged.collectAsStateWithLifecycle()
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "НАСТРОЙКИ ПОЛЬЗОВАТЕЛЯ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF690005),
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .verticalScroll(scrollState) // потом поменяем
                ) {
                    Image(
                        painter = painterResource(R.drawable.cot),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text("${curUser!!.name} ${curUser.surname}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF690005),
                    )
                Spacer(Modifier.height(8.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            onLogout()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF690005)
                        )
                    ) {
                        Text(
                            text = "Выйти",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onEditAccount,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(color = 0xFF690005)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(color = 0xFF690005),
                                Color(color = 0xFF93000A)
                            )
                        )
                    )
                ) {
                    Text(
                        text = "Изменить",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.width(12.dp))

                OutlinedButton(
                    onClick = onOpenAccountInfo,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(color = 0xFF690005)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(color = 0xFF690005),
                                Color(color = 0xFF93000A)
                            )
                        )
                    )
                ) {
                    Text(
                        text = "О пользователе",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onAddPet,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(color = 0xFF690005)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(color = 0xFF690005),
                                Color(color = 0xFF93000A)
                            )
                        )
                    )
                ) {
                    Text(
                        text = "Добавить питомца",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Домашние животные:", style = MaterialTheme.typography.titleMedium, color = Color(0xFF690005))
            Spacer(Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            )
            {
                pets.forEach { pet ->
                    AnimatedVisibility(
                        visible = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        enter = fadeIn() + expandVertically()
                    ) {
                        PetListItem(
                            pet = pet,
                            onClick = onOpenPet
                        )
                    }
                }
            }
        }
    }

    if (uiState.success) {
        goToStart()
    }
}


// Кнопка с анимацией
@Composable
fun AnimatedButton(text: String, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, tween(120))


    Button(
        onClick = onClick,
        interactionSource = interaction,
        modifier = Modifier.scale(scale)
    ) {
        Text(text)
    }
}

@Composable
fun PetListItem(pet: FPet, onClick: (id:String) -> Unit) {
    Log.d("PetListItem", "petId = ${pet.id}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(pet.id) }),
        colors = CardDefaults.cardColors(
            containerColor = Color(color = 0xFFFFE0B2)
        ),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(color = 0xFF690005),
                    Color(color = 0xFF93000A)
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )
        {
            Row (
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )
            {
                Column(modifier = Modifier.weight(1f)) {
                    Text(pet.name, fontWeight = FontWeight.Bold, color = Color(color = 0xFF690005))
                    Text(pet.type.name, color = Color(color = 0xFF690005))
                }
                Text("→", color = Color(color = 0xFF690005))
            }
        }
    }



    /*Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.7f))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            //fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            color = Color(color = 0xFF4A4A4A)
        )
    }*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInfoScreen(onBack: () -> Unit = {},
                      viewModel: AccountViewModel = viewModel(factory = AppViewModelProvider.Factory),
                      onEditAccount :() ->Unit = {},
                      modifier: Modifier = Modifier) {
    val curUser:FUser? = viewModel.userManager.currentUser.collectAsState().value?.user
    val pets = viewModel.userManager.pets.collectAsState().value
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Информация об аккаунте",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF690005),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .background(brush = Constants.GRADIENT_BRUSH)
            .fillMaxWidth()
            .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Телефон: ${curUser!!.phoneNumber}")
            Spacer(Modifier.height(7.dp))
            Text("Email: ${curUser.login}",
                color = Color(0xFF000000))
            Spacer(Modifier.height(7.dp))
            Text("Адрес: ${curUser.address}",
                color = Color(0xFF000000))
            Spacer(Modifier.height(7.dp))
            Text("Дата рождения ${curUser.birthDate}",
                color = Color(0xFF000000))
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { onEditAccount() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(start = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF690005),
                    contentColor = Color.White
                )
            ) {
                Text("Изменить информацию")
            }
        }
    }
}


//питомец
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetInfoScreen(onBack: () -> Unit, petId: String?) {
    val viewModel: PetViewModel = viewModel(factory = PetViewModelFactory(petId!!))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.isLoading) {
        Text("Загрузка...")
    } else if (uiState.success) {
        val pet = uiState.pet
        val name: String = uiState.pet.name
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(name) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(painterResource(id = android.R.drawable.ic_media_previous), contentDescription = "Назад")
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier
                .padding(padding)
                .padding(16.dp)) {
                GetPhotoPet(pet)
                Text("Тип: ${pet.type}")
                Text("Пол: ${pet.gender}")
                Text("Возраст: ${pet.age}")
                Text("Порода: ${pet.breed}")
                Text("Особенности: ${pet.description}")
                Spacer(Modifier.height(20.dp))
                AnimatedButton(text = "Изменить", onClick = {})
            }
        }
    } else {
        Text(uiState.error)
    }
}

@Composable
fun GetPhoto(currentUser: FUser?,
             baseUrl: String = NetworkConfig.BASE_URL
) {
    val fileId: String? = currentUser?.photoId
    if (fileId.isNullOrBlank()) {
        Box(
            Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        return
    }
    AsyncImage(
        model = "$baseUrl/photo/$fileId",
        contentDescription = "photo",
        modifier = Modifier
            .size(160.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun GetPhotoPet(currentPet: FPet?,
             baseUrl: String = NetworkConfig.BASE_URL
) {
    val fileId: String? = currentPet?.photoId
    if (fileId.isNullOrBlank()) {
        Box(
            Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        return
    }
    AsyncImage(
        model = "$baseUrl/photo/$fileId",
        contentDescription = "photo",
        modifier = Modifier
            .size(160.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
        contentScale = ContentScale.Crop,
    )
}

