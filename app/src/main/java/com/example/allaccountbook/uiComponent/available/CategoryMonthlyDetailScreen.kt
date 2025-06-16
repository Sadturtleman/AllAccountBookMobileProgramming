package com.example.allaccountbook.uiComponent.available

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.uiComponent.toYearMonth
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*


@Composable
fun CategoryMonthlyDetailScreen(
    navController: NavController,
    selectedDate: String,             // "2025년 5월" 형식
    selectedCategory: String,         // 예: "음식점"
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    var showDateDialog by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(selectedDate) }

    val selectedYearMonth = remember(selectedMonth) {
        try {
            val parsed = SimpleDateFormat("yyyy년 M월", Locale.KOREA).parse(selectedMonth)
            val formatted = SimpleDateFormat("yyyy-MM", Locale.KOREA).format(parsed!!)
            YearMonth.parse(formatted)
        } catch (e: Exception) {
            null
        }
    }

    val transactions by transactionViewModel.transactions.collectAsState()
    val expenseList = remember(transactions, selectedYearMonth, selectedCategory) {
        transactions
            .filterIsInstance<TransactionDetail.Expense>()
            .filter {
                selectedYearMonth != null && it.data.date.toYearMonth() == selectedYearMonth &&
                        (
                                if (selectedCategory == "기타") {
                                    it.data.category !in listOf("음식점", "문화시설")
                                } else {
                                    it.data.category == selectedCategory
                                }
                                )
            }

    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedMonth,
                modifier = Modifier
                    .clickable { showDateDialog = true }
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(selectedCategory, style = MaterialTheme.typography.titleMedium)
        }

        CustomDatePickerDialog(
            showDialog = showDateDialog,
            onDismiss = { showDateDialog = false },
            onDateSelected = {
                selectedMonth = it // 이미 "yyyy년 M월" 형식
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("사유", modifier = Modifier.weight(1f))
            Text("금액", modifier = Modifier.weight(1f))
            Text("날짜", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(expenseList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.data.name, modifier = Modifier.weight(1f))
                    Text("${item.data.price}원", modifier = Modifier.weight(1f))
                    Text(item.data.date.toString(), modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }
}
