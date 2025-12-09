package com.example.pethelper.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pethelper.R

data class Pet(
    val id: Int,
    val name: String,
    val age: Int,
    val breed: String,
    val specialFeatures: String
)

@Composable
fun AccountInfoScreen(
    avatarResId: Int = 0,
    nickname: String,
    fullName: String,
    pets: List<Pet>,
    onEditAccountClick: () -> Unit,
    onEditPetClick: (Pet) -> Unit,
    onPetClick: (Pet) -> Unit
) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = onEditAccountClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Изменить информацию об аккаунте")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Аватар и основная информация
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Аватар
                Image(
                    painter = painterResource(R.drawable.cot),
                    contentDescription = "Аватар пользователя",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Никнейм
                Text(
                    text = nickname,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                // Имя и фамилия
                Text(
                    text = fullName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Список домашних животных
            Text(
                text = "Мои питомцы",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(pets) { pet ->
                    PetCard(
                        pet = pet,
                        onEditClick = { onEditPetClick(pet) },
                        onClick = { onPetClick(pet) }
                    )
                }
            }
        }
    }
}

@Composable
fun PetCard(
    pet: Pet,
    onEditClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Кличка
                    Text(
                        text = pet.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Возраст и порода
                    Text(
                        text = "${pet.age} лет • ${pet.breed}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Особенности
                    Text(
                        text = "Особенности: ${pet.specialFeatures}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Кнопка редактирования
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Изменить информацию о животном"
                    )
                }
            }
        }
    }
}


@Preview
// Пример использования
@Composable
fun AccountInfoScreenPreview() {
    val samplePets = listOf(
        Pet(
            id = 1,
            name = "Барсик",
            age = 3,
            breed = "Британская короткошерстная",
            specialFeatures = "Любит спать на подоконнике"
        ),
        Pet(
            id = 2,
            name = "Шарик",
            age = 5,
            breed = "Лабрадор",
            specialFeatures = "Очень дружелюбный, любит играть с мячиком"
        )
    )

    AccountInfoScreen( // Я хуй знает, не будет работать без R
        nickname = "cat_lover",
        fullName = "Иван Иванов",
        pets = samplePets,
        onEditAccountClick = { /* Обработка нажатия на редактирование аккаунта */ },
        onEditPetClick = { pet -> /* Обработка нажатия на редактирование животного */ },
        onPetClick = { pet -> /* Обработка нажатия на карточку животного */ }
    )
}