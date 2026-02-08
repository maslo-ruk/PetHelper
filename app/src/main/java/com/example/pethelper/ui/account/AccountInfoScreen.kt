package com.example.pethelper.ui.account

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.ui.AppViewModelProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
    viewModel: StartViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onLogout:() -> Unit = viewModel::logout,
    onEditAccount: () -> Unit = {},
    onOpenAccountInfo: () -> Unit = {},
    onOpenPet: (petId: String) -> Unit = {},
    onAddPet: () -> Unit = {},
    onBack:() -> Unit = {}
) {
    val alphaAnim = animateFloatAsState(1f, tween(800, easing = LinearOutSlowInEasing))
    val curUser:FUser? = viewModel.userManager.currentUser.collectAsState().value?.user
    val pets = viewModel.userManager.pets.collectAsState().value
    val petUiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Constants.GRADIENT_BRUSH)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "Назад",
                tint = Color(0xFF690005))
        }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
                    .alpha(alphaAnim.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
// Аватар
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .verticalScroll(rememberScrollState()) // потом поменяем
                ) {
                    Image(
                        painter = painterResource(R.drawable.cot),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.height(12.dp))
                Text("${curUser!!.name} ${curUser.surname}", fontWeight = FontWeight.Bold)

                TextButton(
                    onClick = {
                        onLogout()
                    },
                    modifier = Modifier,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF93000A)
                    )
                ) {
                    Text(
                        text = "Выйти",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    AnimatedButton(text = "Изменить аватар или имя",
                        onClick = onEditAccount,
                        )
                    Spacer(Modifier.height(12.dp))


                    AnimatedButton(text = "Информация об аккаунте", onClick = onOpenAccountInfo)
                }

                Spacer(Modifier.height(12.dp))


                Button(onClick = {onAddPet()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .padding(start = 20.dp, end = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(color = 0xFF690005),
                        contentColor = Color.White
                    )
                ) {
                Text("Добавить питомца",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold)
            }


                Spacer(Modifier.height(24.dp))
                Text("Домашние животные", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))


                LazyColumn {
                    items(pets) { pet ->
                        AnimatedVisibility(
                            visible = true,
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(pet.name, fontWeight = FontWeight.Bold)
                Text(pet.type.name)
            }
            Text("→", color = MaterialTheme.colorScheme.primary)
        }
    }
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
            TopAppBar(
                title = { Text("Информация об аккаунте") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(id = android.R.drawable.ic_media_previous), contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Телефон: ${curUser!!.phoneNumber}")
            Text("Email: ${curUser.login}")
            Spacer(Modifier.height(12.dp))
            Text("Адрес: ${curUser.address}")
            Spacer(Modifier.height(12.dp))
            Text("Дата рождения ${curUser.birthDate}")
            Spacer(Modifier.height(20.dp))
            AnimatedButton(text = "Изменить информацию", onClick = {onEditAccount()})
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

