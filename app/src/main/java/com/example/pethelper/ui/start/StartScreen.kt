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
                    modifier = Modifier.height(180.dp).width(180.dp)
                )
            }
        }


        Button(
            onClick = onCreateOrder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать заказ")
        }
        
        Button(
            onClick = {
                onLogout()
                      },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти")
        }
        Button(
            onClick = {
                onLoc()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Чек геолокации")
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
        Row(
            modifier = Modifier.width(80.dp),
            horizontalArrangement = Arrangement.End
        ){
            Button(
                onClick = {
                    onChat()
                },
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
            ) {
                Text("Мои чаты")
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
        ) {
            Text("Войти")
        }

        Button(
            onClick = onReg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Регистрация")
        }
    }
}


