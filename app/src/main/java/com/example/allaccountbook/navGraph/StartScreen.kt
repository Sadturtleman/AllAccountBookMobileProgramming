package com.example.allaccountbook.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun StartScreen() {
    val navController = rememberNavController()
    RootNavGraph(navController= navController)
}