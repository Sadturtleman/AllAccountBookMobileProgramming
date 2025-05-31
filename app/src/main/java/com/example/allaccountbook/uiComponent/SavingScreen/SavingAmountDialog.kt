package com.example.allaccountbook.uiComponent.SavingScreen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview

import androidx.navigation.compose.rememberNavController

@Preview(showBackground = true)
@Composable
fun SavingAmountDialogPreview() {
    val navController = rememberNavController()
    SavingAmountDialog(
        navController = navController,
        onDismiss = {}
    )
}

@Composable
fun SavingAmountDialog(
    navController: NavController,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .widthIn(max = 560.dp)
            ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Button(
                    onClick = {
                        onDismiss()
                        navController.navigate("savingAmountDetail")
                    }
                ) {
                    Text("자세히 보기")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("저축명 : 여행 적금")
                Spacer(modifier = Modifier.height(8.dp))
                Text("저축 금액 : 30,000원")
            }
        }
    }
}
