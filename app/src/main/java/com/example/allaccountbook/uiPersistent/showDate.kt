package com.example.allaccountbook.uiPersistent

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun showDate(showDate : String) {
    Text(showDate, fontSize = 20.sp, fontWeight = FontWeight.Bold)
}