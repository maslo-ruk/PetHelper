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
import com.example.pethelper.ui.RegistrationScreen
import com.example.pethelper.ui.account.AccountChange
import com.example.pethelper.ui.account.AccountInfoScreen
import com.example.pethelper.ui.start.AuthFalse
import com.example.pethelper.ui.start.AuthTrue

import com.example.pethelper.ui.start.MainScreen
import com.example.pethelper.ui.account.AccountScreen
import com.example.pethelper.ui.account.PetInfoScreen
import com.example.pethelper.ui.auth.LoginScreen

import com.example.pethelper.ui.orders.OrderDialog
import com.example.pethelper.ui.ordersView.AvailableOrders
import com.example.pethelper.ui.pets.PetCreate
import com.example.pethelper.ui.walk.LocationPermissionScreen
import com.example.pethelper.ui.walk.WalkScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

enum class PetHelperScreens {
    Loading,
    Start,
    StartAuth,
    StartForClients,
    StartForHelpers,
    HelpersCatalogue,
    Order,
    Account,
    AccountInfo,
    AccountEdit,
    AddPet,
    PetInfo,
    Reg,
    Auth,
    Location,
    Permissions,
    OrdersAvailable
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
                    onLogin = {navController.navigate(PetHelperScreens.Auth.name)},
                    onReg = {navController.navigate(PetHelperScreens.Reg.name)},
                    onCreateOrder = {navController.navigate(PetHelperScreens.Order.name)},
                    onAccount =  {navController.navigate(PetHelperScreens.Account.name)},
                    onLoc = { navController.navigate(PetHelperScreens.Location.name) },
                    onShowOrders = { navController.navigate(PetHelperScreens.OrdersAvailable.name) }
                )
            }
            composable(PetHelperScreens.Order.name) {
                OrderDialog(Modifier, onClose = { navController.popBackStack() })
            }
            composable(PetHelperScreens.Account.name) {
                AccountScreen(
                    onBack = {navController.popBackStack()},
                    onOpenAccountInfo = {navController.navigate(PetHelperScreens.AccountInfo.name)},
                    onAddPet = {navController.navigate(PetHelperScreens.AddPet.name)},
                    onOpenPet = {navController.navigate(PetHelperScreens.PetInfo.name)}
                )
            }
            composable(PetHelperScreens.AccountInfo.name) {
                AccountInfoScreen(
                    onBack = {navController.popBackStack()},
                    onEditAccount = {navController.navigate(PetHelperScreens.AccountEdit.name)}
                )
            }
            composable(PetHelperScreens.Reg.name) {
                RegistrationScreen(
                    onBack = { navController.popBackStack() },
                    onLoginClick = {navController.navigate(PetHelperScreens.Auth)},
                    goToMain = {navController.navigate(PetHelperScreens.Start.name)}
                )
            }
            composable(PetHelperScreens.Auth.name) {
                LoginScreen({navController.popBackStack()}, goToMain = {navController.navigate(PetHelperScreens.Start.name)})
            }
            composable(PetHelperScreens.AccountEdit.name) {
                AccountChange(onBack = {navController.popBackStack()})
            }
            composable(PetHelperScreens.AddPet.name) {
                PetCreate(onBack = {navController.popBackStack()})
            }
            composable(route = PetHelperScreens.PetInfo.name) {
                PetInfoScreen(onBack = {navController.popBackStack()})
            }
            composable(route = PetHelperScreens.Location.name) {
                WalkScreen()
            }
            composable(PetHelperScreens.OrdersAvailable.name) {
                AvailableOrders()
            }
        }
    }
}