package com.example.allaccountbook.NavGraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun StartScreen() {
    val navController = rememberNavController()
    RootNavGraph(navController= navController)
}