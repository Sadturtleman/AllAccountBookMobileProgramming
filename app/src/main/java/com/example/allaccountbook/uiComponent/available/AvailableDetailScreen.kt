package com.example.allaccountbook.uiComponent.available

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.uiComponent.toYearMonth
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.viewmodel.view.AvailableViewModel
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*

data class MonthlyCategoryInfo(
    val month: String,
    val categoryName: String,
    val remainingAmount: Int,
    val maxAmount: Int
) {
    val remainingPercent: Int
        get() = if (maxAmount == 0) 0 else (remainingAmount * 100 / maxAmount)
}

@Preview(showBackground = true)
@Composable
fun AvailableDetailScreenPreview() {
    val navController = rememberNavController()
    AvailableDetailScreen(navController, "2025년 5월")
}

@Composable
fun AvailableDetailScreen(
    navController: NavController,
    selectedDate: String,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    availableViewModel: AvailableViewModel = viewModel()
) {
    var editingCategory by remember { mutableStateOf<String?>(null) }
    var input by remember { mutableStateOf("") }

    val selectedMonth = selectedDate // 텍스트만 표시
    val selectedYearMonth = remember(selectedMonth) {
        try {
            val parsed = SimpleDateFormat("yyyy년 M월", Locale.KOREA).parse(selectedMonth)
            val formatted = SimpleDateFormat("yyyy-MM", Locale.KOREA).format(parsed!!)
            YearMonth.parse(formatted)
        } catch (e: Exception) {
            null
        }
    }

    val transactions = transactionViewModel.transactions.collectAsState()
    val predefinedCategories = listOf("음식점", "문화시설")
    val categoryAmountMap = availableViewModel.categoryAmountMap

    LaunchedEffect(Unit) {
        (predefinedCategories + "기타").forEach { availableViewModel.loadAmount(it) }
    }

    val monthlyCategoryInfoList =
        remember(transactions.value, selectedYearMonth, categoryAmountMap) {
            val expenseList = transactions.value
                .filterIsInstance<TransactionDetail.Expense>()
                .map { it.data }
                .filter { selectedYearMonth != null && it.date.toYearMonth() == selectedYearMonth }

            buildList {
                for (category in predefinedCategories) {
                    val spent = expenseList.filter { it.category == category }.sumOf { it.price }
                    val max = categoryAmountMap[category] ?: 0
                    add(
                        MonthlyCategoryInfo(
                            month = selectedMonth,
                            categoryName = category,
                            remainingAmount = (max - spent).coerceAtLeast(0),
                            maxAmount = max
                        )
                    )
                }

                val otherSpent =
                    expenseList.filter { it.category !in predefinedCategories }.sumOf { it.price }
                val max = categoryAmountMap["기타"] ?: 0
                add(
                    MonthlyCategoryInfo(
                        month = selectedMonth,
                        categoryName = "기타",
                        remainingAmount = (max - otherSpent).coerceAtLeast(0),
                        maxAmount = max
                    )
                )
            }
        }

    val averagePercent = if (monthlyCategoryInfoList.isEmpty()) 0
    else monthlyCategoryInfoList.map { it.remainingPercent }.average().toInt()

    val totalRemaining = monthlyCategoryInfoList.sumOf { it.remainingAmount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = selectedMonth,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = averagePercent / 100f)
                    .fillMaxHeight()
                    .background(Color(0xFFFFC107), RoundedCornerShape(8.dp))
            )
            Text(
                text = "평균 남은 비율: $averagePercent%",
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("카테고리", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text("설정 금액", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text("남은 금액", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text("남은 비율", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        }


        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(monthlyCategoryInfoList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        item.categoryName,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate("categoryMonthlyDetail/$selectedDate/${item.categoryName}")
                            }
                    )
                    Text(
                        text = "${item.maxAmount}원",
                        modifier = Modifier
                            .weight(1f)
                            .clickable { editingCategory = item.categoryName }
                            .background(Color(0xFFE0F7FA), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                    Text(
                        "${item.remainingAmount}원",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "${item.remainingPercent}%",
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFA8F0A3), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text("전체 남은 금액: ${totalRemaining}원", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }

    if (editingCategory != null) {
        AlertDialog(
            onDismissRequest = { editingCategory = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newAmount = input.toIntOrNull()
                        if (newAmount != null) {
                            availableViewModel.saveAmount(editingCategory!!, newAmount)
                            editingCategory = null
                        }
                    }
                ) {
                    Text("저장")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingCategory = null }) {
                    Text("취소")
                }
            },
            title = { Text("${editingCategory} 설정 금액 변경") },
            text = {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("금액") }
                )
            }
        )
    }
}
