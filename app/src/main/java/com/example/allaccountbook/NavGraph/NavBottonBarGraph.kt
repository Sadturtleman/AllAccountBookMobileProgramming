package com.example.allaccountbook.NavGraph

import MainScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.allaccountbook.uiComponent.Available.AvailableDetailScreen
import com.example.allaccountbook.uiComponent.ShowDailyScreen
import com.example.allaccountbook.uiPersistent.BottomNavBar

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

