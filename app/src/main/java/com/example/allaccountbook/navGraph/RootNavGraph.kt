package com.example.allaccountbook.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavGraph(navController : NavHostController) {
    NavHost(navController = navController, startDestination = "main"){
        LoginNavGraph(navController)
        NavBottonBarGraph(navController)
    }
}