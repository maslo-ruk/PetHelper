package com.example.pethelper

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pethelper.data.entities.User
import com.example.pethelper.ui.AccountInfoScreen
import com.example.pethelper.ui.MainScreen
import com.example.pethelper.ui.PetInfoScreen
import com.example.pethelper.ui.StartScreen
import com.example.pethelper.ui.orders.OrderDialog

enum class PetHelperScreens {
    Loading,
    Start,
    StartForClients,
    StartForHelpers,
    HelpersCatalogue,
    Order,
    Account
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetHelperApp(
    navController: NavHostController
) {
    Scaffold {
        innerPadding ->

        NavHost(
            navController = navController,
            startDestination = PetHelperScreens.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(PetHelperScreens.Start.name) {
                MainScreen(onCreateOrder = {navController.navigate(PetHelperScreens.Order.name)})
            }
            composable(PetHelperScreens.Order.name) {
                OrderDialog(Modifier, onClose = { navController.popBackStack() }, currentUser = User(0, 1,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ))
            }
            composable(PetHelperScreens.Account.name) {
                AccountInfoScreen()
            }
        }
    }
}