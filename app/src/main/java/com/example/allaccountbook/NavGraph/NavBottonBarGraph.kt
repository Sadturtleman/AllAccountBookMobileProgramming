package com.example.allaccountbook.NavGraph

import com.example.allaccountbook.uiComponent.MainScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.allaccountbook.map.TransactionMapScreen
import com.example.allaccountbook.uiComponent.ShowDaily.ShowDailyScreen

fun NavGraphBuilder.NavBottonBarGraph(navController: NavHostController) {
    composable("home") {
        MainScreen(navController = navController)
    }
    composable("date") {
        ShowDailyScreen(
            navController = navController
        )
    }
    composable("map") {
        TransactionMapScreen()
    }
}

