package com.example.allaccountbook.uiPersistent

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.text.DecimalFormat

@Composable
fun formatWithCommas(amount: Int): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(amount)
}