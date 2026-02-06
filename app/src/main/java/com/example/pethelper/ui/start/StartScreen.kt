package com.example.pethelper.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.ui.AppViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pethelper.data.fireBaseEntities.FUser
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.Image
@Composable
fun MainScreen(
    viewModel: StartViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onLogout:() -> Unit= viewModel::logout,
    onCreateOrder:() -> Unit,
    onAccount: () -> Unit,
    onLogin: () -> Unit,
    onLoc:() -> Unit,
    onReg: () -> Unit,
    onShowOrders:() -> Unit = {},
    onShowOrdersUser:() -> Unit = {},
    onChat:()->Unit
) {
    val isLogged by viewModel.isLogged.collectAsStateWithLifecycle()
    val curUser:FUser? = viewModel.userManager.currentUser.collectAsState().value?.user
    if (isLogged) {
        if (curUser == null) {
            Text("Загрузка пользователя")
        }
        else {
            AuthTrue(onCreateOrder, onLogout, onAccount, onLoc, curUser, onShowOrders, onShowOrdersUser
            ,onChat)
        }
    }
    else {
        AuthFalse(onLogin, onReg)
    }
}

@Composable
fun AuthTrue(onCreateOrder: () -> Unit, onLogout:() -> Unit, onAccount:() -> Unit, onLoc: () -> Unit,
             curUser:FUser,
             onShowOrders:() -> Unit = {},
             onShowOrdersUser:() -> Unit = {},
             onChat:()->Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 60.dp, bottom = 0.dp, start = 40.dp, end = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "PetHelper",
            )

            IconButton(onClick = { onAccount() }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Аккаунт",
                    modifier = Modifier.size(400.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = onCreateOrder,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF93000A),
                contentColor = Color.White  // цвет текста
            )
        ) {
            Text("Создать заказ")
        }

        if (curUser.type == 0) {
            Button(
                onClick = {
                    onShowOrders()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF93000A),
                    contentColor = Color.White  // цвет текста
                )
            ) {
                Text("Доступные заказы")
            }
        }
        else {
            Button(
                onClick = {
                    onShowOrdersUser()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF93000A),
                    contentColor = Color.White  // цвет текста
                )
            ) {
                Text("Данные по заказам")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                onLoc()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF93000A),
                contentColor = Color.White  // цвет текста
            )
        ) {
            Text("Геолокация")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                onLogout()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF93000A),
                contentColor = Color.White  // цвет текста
            )
        ) {
            Text("Выйти")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Button(
                onClick = {
                    onChat()
                },
                shape = CircleShape,
                modifier = Modifier.size(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF93000A),
                    contentColor = Color.White  // цвет текста
                )
            ) {
                // Иконка и текст в ряд
                Icon(
                    imageVector = Icons.Default.Chat, // Иконка чата из Material Icons
                    contentDescription = "Чат",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }

}

@Composable
fun AuthFalse(onLogin: () -> Unit, onReg: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp, start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Spacer(Modifier.height(60.dp))

//        Text(
//            text = "Зарегистрируйтесь, или войдите в аккаунт"
//        )
        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF93000A),
                contentColor = Color.White  // цвет текста
            )
        ) {
            Text("Войти")
        }

        Button(
            onClick = onReg,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF93000A),
                contentColor = Color.White  // цвет текста
            )
        ) {
            Text("Регистрация")
        }
    }
}


