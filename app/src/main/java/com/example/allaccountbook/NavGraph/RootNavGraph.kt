package com.example.allaccountbook.NavGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun RootNavGraph(navController : NavHostController) {
    NavHost(navController = navController, startDestination = "login"){
        LoginNavGraph(navController)
        NavBottonBarGraph(navController)
    }
}