package com.example.allaccountbook.uiComponent.savingScreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TextButton
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
        onDismiss = {},
        savingName = "여행 적금",
        amount = 30000,
        onConfirm = {} as (Int, String) -> Unit

    )
}

@Composable
fun SavingAmountDialog(
    navController: NavController,
    onDismiss: () -> Unit,
    onConfirm: (Int, String) -> Unit,
    savingName: String,
    amount: Int
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
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("저축명 : $savingName")
                Spacer(modifier = Modifier.height(8.dp))
                Text("저축 금액 : %,d원".format(amount))

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        onDismiss()
                        navController.navigate("savingAmountDetail")
                    }) {
                        Text("자세히 보기")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        onConfirm(amount, savingName)
                        onDismiss()
                    }) {
                        Text("확인")
                    }
                }
            }
        }
    }
}
