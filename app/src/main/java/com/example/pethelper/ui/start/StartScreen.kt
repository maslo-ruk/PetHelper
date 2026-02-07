package com.example.pethelper.ui.start

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.animation.core.tween
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pethelper.data.fireBaseEntities.FUser
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.filled.Chat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import com.example.pethelper.Constants
import com.example.pethelper.R
import androidx.compose.material3.TextButton

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Constants.GRADIENT_BRUSH)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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

                IconButton(onClick = { onAccount() }) {
                    Icon(
                        Icons.Default.AccountCircle, contentDescription = "Аккаунт",
                        modifier = Modifier.size(400.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = onCreateOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(color = 0xFF690005),
                    contentColor = Color.White
                )
            ) {
                Text("Создать заказ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (curUser.type == 0) {
                Button(
                    onClick = {
                        onShowOrders()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(color = 0xFF690005),
                        contentColor = Color.White
                    )
                ) {
                    Text("Доступные заказы",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            } else {
                Button(
                    onClick = {
                        onShowOrdersUser()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(color = 0xFF690005),
                        contentColor = Color.White
                    )
                ) {
                    Text("Данные по заказам",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    onLoc()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(color = 0xFF690005),
                    contentColor = Color.White
                )
            ) {
                Text("Геолокация",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onChat()
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(color = 0xFF690005),
                        contentColor = Color.White
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
}

@Composable
fun AuthFalse(onLogin: () -> Unit, onReg: () -> Unit) {

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale: Float by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Constants.GRADIENT_BRUSH)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(top = 80.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(Color(color = 0xFF690005)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.doglogo1),
                        contentDescription = "Лапка",
                        modifier = Modifier.size(150.dp)
                            .clip(CircleShape),
                        tint = Color.Unspecified
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FeatureItem(emoji = "🐕", text = "Отслеживание прогулки в реальном времени")

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(color = 0xFF690005),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Войти",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onReg,
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
                        text = "Регистрация",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
/*
@Composable
fun AuthFalse2(onLogin: () -> Unit, onReg: () -> Unit) {

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale: Float by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    var visible: Boolean by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        delay(300L)
        visible = true
    }

    val alpha: Float by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(color = 0xFFFFF8F0),
                        Color(color = 0xFFFFE0B2),
                        Color(color = 0xFFFFF8F0)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(top = 80.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Верхняя часть
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(Color(color = 0xFF690005)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.doglogo1),
                        //imageVector = ImageVector.Builder(painterResource(id = R.drawable.doglogo1), contentDescription = "Лапка"),// R.drawable.doglogo1,
                        contentDescription = "Лапка",
                        modifier = Modifier.size(120.dp)
                            .clip(CircleShape),
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "PetWalk",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    //fontFamily = Montserrat,
                    color = Color(color = 0xFF690005)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Прогулки с заботой\nо вашем питомце",
                    fontSize = 16.sp,
                    //fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = Color(color = 0xFF93000A),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            // Средняя часть
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FeatureItem(emoji = "🐕", text = "Надёжные выгульщики рядом с вами")
                //FeatureItem(emoji = "📍", text = "Отслеживание прогулки в реальном времени")
                //FeatureItem(emoji = "⭐", text = "Рейтинги и отзывы от владельцев")
            }

            // Нижняя часть
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onReg,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(color = 0xFF690005),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Зарегистрироваться",
                        fontSize = 16.sp,
                        //fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                //Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onLogin,
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
                        text = "Войти",
                        fontSize = 16.sp,
                        //fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold
                    )
                }
/*
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Продолжая, вы соглашаетесь с условиями использования",
                    fontSize = 12.sp,
                    //fontFamily = Montserrat,
                    color = Color(color = 0xFFAAAAAA),
                    textAlign = TextAlign.Center
                )*/
            }
        }
    }
}*/

@Composable
fun FeatureItem(emoji: String, text: String) {
    Row(
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
    }
}