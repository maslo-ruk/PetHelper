package com.example.pethelper

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pethelper.ui.account.AccountInfoScreen
import com.example.pethelper.ui.AuthFalse
import com.example.pethelper.ui.AuthTrue

import com.example.pethelper.ui.MainScreen
import com.example.pethelper.ui.auth.LoginScreen
import com.example.pethelper.ui.auth.RegistrationScreen
import com.example.pethelper.ui.orders.OrderDialog
import com.google.firebase.auth.FirebaseAuth

enum class PetHelperScreens {
    Loading,
    Start,
    StartAuth,
    StartNOAuth,
    StartForClients,
    StartForHelpers,
    HelpersCatalogue,
    Order,
    Account,
    Reg,
    Auth
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetHelperApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    auth: FirebaseAuth
) {
    Scaffold {
        innerPadding ->

        NavHost(
            navController = navController,
            startDestination = PetHelperScreens.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(PetHelperScreens.Start.name) {
                MainScreen(
                    auth = auth,
                    ifAuth = {navController.navigate(PetHelperScreens.StartAuth.name)},
                    ifNoAuth = {navController.navigate(PetHelperScreens.StartNOAuth.name)}
                )
            }
            composable(route= PetHelperScreens.StartAuth.name) {
                AuthTrue(
                    onCreateOrder = {navController.navigate(PetHelperScreens.Order.name)},
                    onLogout = { auth.signOut() },
                    onAccount = {navController.navigate(PetHelperScreens.Account.name)},
                    onBack = { navController.popBackStack() }
                )
            }
            composable(route= PetHelperScreens.StartNOAuth.name) {
                AuthFalse(
                    onReg = {navController.navigate(PetHelperScreens.Reg.name)},
                    onLogin = {navController.navigate(PetHelperScreens.Auth.name)},
                    onBack = { navController.popBackStack() }
                )
            }
            composable(PetHelperScreens.Order.name) {
                OrderDialog(Modifier, onClose = { navController.popBackStack() })
            }
            composable(PetHelperScreens.Account.name) {
                AccountInfoScreen(
                    onBack = {navController.popBackStack()}
                )
            }
            composable(PetHelperScreens.Reg.name) {
                RegistrationScreen(
                    onBack = { navController.popBackStack() },
                    onLoginClick = {navController.navigate(PetHelperScreens.Auth)}
                )
            }
            composable(PetHelperScreens.Auth.name) {
                LoginScreen({navController.popBackStack()})
            }
        }
    }
}