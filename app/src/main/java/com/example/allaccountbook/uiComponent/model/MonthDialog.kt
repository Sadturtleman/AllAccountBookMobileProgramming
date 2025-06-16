package com.example.allaccountbook.uiComponent.model

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Calendar
import androidx.compose.foundation.layout.Column
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun YearMonthPickerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    if (showDialog) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

        var selectedYear by remember { mutableIntStateOf(currentYear) }
        var selectedMonth by remember { mutableIntStateOf(currentMonth) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("연도와 월 선택") },
            text = {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 연도 선택
                        DropdownMenuBox(
                            label = "연도",
                            options = (currentYear - 10..currentYear + 10).toList(),
                            selected = selectedYear,
                            onSelected = { selectedYear = it }
                        )

                        // 월 선택
                        DropdownMenuBox(
                            label = "월",
                            options = (1..12).toList(),
                            selected = selectedMonth,
                            onSelected = { selectedMonth = it }
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, selectedYear)
                        set(Calendar.MONTH, selectedMonth - 1)
                    }
                    val formatted = SimpleDateFormat("yyyy년 M월", Locale.KOREA).format(calendar.time)
                    onDateSelected(formatted)  // "2025년 5월" 형태로 전달
                    onDismiss()
                }) {
                    Text("확인")
                }
            }
            ,
            dismissButton = {
                OutlinedButton(onClick = onDismiss) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
fun DropdownMenuBox(
    label: String,
    options: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            "$label: $selected",
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
                .padding(8.dp)
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
