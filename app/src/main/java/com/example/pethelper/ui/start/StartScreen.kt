package com.example.pethelper.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pethelper.data.fireBaseEntities.FUser

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
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "PetHelper",
            )

            IconButton(onClick = { onAccount() }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Аккаунт")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("")
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onCreateOrder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать заказ")
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                onLogout()
                      },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти")
        }
        if (curUser.type == 0) {
            Button(
                onClick = {
                    onShowOrders()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Доступные заказы")
            }
        }
        else {
            Button(
                onClick = {
                    onShowOrdersUser()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Данные по заказам")
            }
        }
        Button(
            onClick = {
                onChat()
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Мои чаты")
        }
    }

}

@Composable
fun AuthFalse(onLogin: () -> Unit, onReg: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Зарегистрируйтесь, или войдите в аккаунт"
        )
        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
        Button(
            onClick = onReg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }
    }
}


