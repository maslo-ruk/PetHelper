package com.example.pethelper

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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

            }
            composable(PetHelperScreens.Order.name) {

            }
            composable(PetHelperScreens.Account.name) {

            }
        }
    }
}