package com.example.pethelper.ui.account

import android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.ui.AppViewModelProvider
import com.example.pethelper.ui.auth.LoginViewModel
import androidx.compose.runtime.collectAsState

data class Pet(
    val id: String,
    val name: String,
    val type: String,
    val gender: String,
    val age: String,
    val breed: String,
    val details: String
)

val samplePets = listOf(
    Pet("1", "Бим", "Собака", "М", "3 года", "Бигль", "Добрый и активный"),
    Pet("2", "Майя", "Кошка", "Ж", "5 лет", "Британская", "Очень спокойная")
)


//@Composable
//fun AccountRoot() {
//    var screen by remember { mutableStateOf("account") }
//    var selectedPet: Pet? by remember { mutableStateOf(null) }
//
//
//    when (screen) {
//        "account" -> AccountScreen(
//            pets = samplePets,
//            onEditAccount = {},
//            onOpenAccountInfo = { screen = "accountInfo" },
//            onOpenPet = {
//                selectedPet = it
//                screen = "pet"
//            },
//            modifier = Modifier
//        )
//
//
//        "accountInfo" -> AccountInfoScreen(onBack = { screen = "account" })
//
//
//        "pet" -> selectedPet?.let { pet ->
//            PetInfoScreen(pet = pet, onBack = { screen = "account" })
//        }
//    }
//}

@Composable
fun AccountScreen(
    pets: List<Pet> = samplePets,
    onEditAccount: () -> Unit = {},
    onOpenAccountInfo: () -> Unit = {},
    onOpenPet: (Pet) -> Unit = {},
    onBack:() -> Unit = {},
    modifier: Modifier,
    viewModel: AccountViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val alphaAnim = animateFloatAsState(1f, tween(800, easing = LinearOutSlowInEasing))
    val curUser:FUser? = viewModel.userManager.currentUser.collectAsState().value
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .alpha(alphaAnim.value)
        ) {
// Аватар
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape) // потом поменяем
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sym_def_app_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }

            Spacer(Modifier.height(12.dp))
            Text("${curUser!!.name} ${curUser.surname}", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))


            AnimatedButton(text = "Изменить аватар или имя", onClick = onEditAccount)
            Spacer(Modifier.height(12.dp))


            AnimatedButton(text = "Информация об аккаунте", onClick = onOpenAccountInfo)


            Spacer(Modifier.height(24.dp))
            Text("Домашние животные", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))


            LazyColumn {
                items(pets) { pet ->
                    AnimatedVisibility(visible = true, enter = fadeIn() + expandVertically()) {
                        PetListItem(pet = pet, onClick = { onOpenPet(pet) })
                    }
                    Spacer(Modifier.height(12.dp))
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
fun PetListItem(pet: Pet, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(pet.name, fontWeight = FontWeight.Bold)
                Text(pet.type)
            }
            Text("→", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInfoScreen(onBack: () -> Unit = {}, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Информация об аккаунте") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(id = R.drawable.ic_media_previous), contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Телефон: +7 ХХХ ХХХ ХХ ХХ")
            Text("Email: example@mail.ru")
            Spacer(Modifier.height(12.dp))
            Text("Район: /*TODO*/")
            Spacer(Modifier.height(12.dp))
            Text("Файл документов: (/*TODO*/)")
            Spacer(Modifier.height(20.dp))
            AnimatedButton(text = "Изменить информацию", onClick = {/*TODO*/})
        }
    }
}


//питомец
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetInfoScreen(pet: Pet, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pet.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(id = R.drawable.ic_media_previous), contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Тип: ${pet.type}")
            Text("Пол: ${pet.gender}")
            Text("Возраст: ${pet.age}")
            Text("Порода: ${pet.breed}")
            Text("Особенности: ${pet.details}")
            Spacer(Modifier.height(20.dp))
            AnimatedButton(text = "Изменить", onClick = {})
        }
    }
}

