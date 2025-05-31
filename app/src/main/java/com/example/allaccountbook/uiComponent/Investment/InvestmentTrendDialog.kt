package com.example.allaccountbook.uiComponent.Investment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InvestmentTrendDialog(
    onDismiss: () -> Unit,
    navController: NavController,
    trendName: String = "부동산"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onDismiss()
                navController.navigate("investmentTrendDetail")
            }) {
                Text("자세히 보기")
            }
        },
        title = { Text(trendName) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("당월 손익:")
                    Text("100,000원")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("누적 손익:")
                    Text("350,000원")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("보유량:")
                    Text("200,000원")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("보유율:")
                    Text("18.5%")
                }
            }
        }
    )
}
