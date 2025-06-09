package com.example.allaccountbook.NavGraph

import MainScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.allaccountbook.uiComponent.ShowDaily.ShowDailyScreen

fun NavGraphBuilder.NavBottonBarGraph(navController: NavHostController) {
    composable("home") {
        MainScreen(navController)
    }
    composable("date") {
        ShowDailyScreen(
            navController = navController
        )
    }
    composable("map") {
        // TODO: map 화면 작성 시 여기에 넣기
    }
}

