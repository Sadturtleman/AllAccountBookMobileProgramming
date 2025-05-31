package com.example.allaccountbook.Available

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.allaccountbook.uiPersistent.BottomNevBar
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog

@Preview(showBackground = true)
@Composable
fun CategoryMonthlyDetailPreview() {
    val navController = rememberNavController()
    CategoryMonthlyDetailScreen(navController)
}
@Composable
fun CategoryMonthlyDetailScreen(navController: NavController) {
    val state = navController.previousBackStackEntry?.savedStateHandle
    val bank = state?.get<String>("detail_bank") ?: "은행"
    val category = state?.get<String>("detail_category") ?: "카테고리"

    var selectedDate by remember { mutableStateOf("2025-05") }
    var showDateDialog by remember { mutableStateOf(false) }

    data class SpendItem(
        val reason: String,
        val amount: String,
        val confirmed: String,
        val friendSpent: String,
        val date: String
    )

    val dummyList = listOf(
        SpendItem("A", "8000", "O", "X", "2025-05-01"),
        SpendItem("B", "15000", "X", "O", "2025-05-03"),
        SpendItem("C", "4500", "O", "X", "2025-05-05"),
        SpendItem("D", "12000", "O", "X", "2025-04-20"),
        SpendItem("E", "6000", "X", "O", "2025-04-25")
    )

    val filteredList = remember(selectedDate) {
        dummyList.filter { it.date.startsWith(selectedDate) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedDate,
                modifier = Modifier
                    .clickable { showDateDialog = true }
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("$bank / $category", style = MaterialTheme.typography.titleMedium)
        }

        CustomDatePickerDialog(
            showDialog = showDateDialog,
            onDismiss = { showDateDialog = false },
            onDateSelected = {
                selectedDate = it.substring(0, 7) // e.g., "2025년 5월" -> "2025-05"
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("사유", modifier = Modifier.weight(1f))
            Text("금액", modifier = Modifier.weight(1f))
            Text("확인", modifier = Modifier.weight(1f))
            Text("친구들에게", modifier = Modifier.weight(1f))
            Text("날짜", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(filteredList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.reason, modifier = Modifier.weight(1f))
                    Text(item.amount, modifier = Modifier.weight(1f))
                    Text(item.confirmed, modifier = Modifier.weight(1f))
                    Text(item.friendSpent, modifier = Modifier.weight(1f))
                    Text(item.date, modifier = Modifier.weight(1f))
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        BottomNevBar()
    }
}
