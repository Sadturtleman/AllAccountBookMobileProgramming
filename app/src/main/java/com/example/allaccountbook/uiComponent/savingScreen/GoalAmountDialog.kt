package com.example.allaccountbook.uiComponent.savingScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun GoalAmountDialog(
    currentAmount: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var text by remember { mutableStateOf(currentAmount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("목표 금액 설정") },
        text = {
            Column {
                Text("원하는 목표 금액을 입력하세요")
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        // 숫자만 허용
                        if (it.all { c -> c.isDigit() }) text = it
                    },
                    placeholder = { Text("예: 100000") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newAmount = text.toIntOrNull() ?: currentAmount
                    onConfirm(newAmount)
                }
            ) { Text("확인") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
