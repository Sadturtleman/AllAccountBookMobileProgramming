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
    selectedDate: String,         // 예: "2025년 6월"
    selectedCategory: String,     // 예: "음식점"
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
    // 날짜로 그룹화
    val grouped = expenseList.groupBy {
        SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(it.data.date)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        // 카테고리 명
        Text(
            text = selectedCategory,
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, bottom = 12.dp),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        CustomDatePickerDialog(
            showDialog = showDateDialog,
            onDismiss = { showDateDialog = false },
            onDateSelected = {
                selectedMonth = it
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 리스트
        LazyColumn {
            grouped.forEach { (dateStr, itemsOnDate) ->
                item {
                    Text(
                        text = dateStr,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(itemsOnDate) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.data.name)
//后续有时间的话可以添加 시간이 있으면 나중에 추가할 수 있습니다.
//                            Text(
//                                text = SimpleDateFormat("HH:mm", Locale.KOREA).format(item.data.date),
//                                style = MaterialTheme.typography.bodySmall,
//                                color = Color.Gray
//                            )
                        }

                        Text(
                            text = "%,d원".format(item.data.price),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

    // Column(modifier = Modifier
    //     .fillMaxSize()
    //     .padding(16.dp)) {
    //     Row(
    //         modifier = Modifier.fillMaxWidth(),
    //         verticalAlignment = Alignment.CenterVertically
    //     ) {
    //         Text(
    //             text = selectedMonth,
    //             modifier = Modifier
    //                 .clickable { showDateDialog = true }
    //                 .padding(8.dp),
    //             style = MaterialTheme.typography.bodyLarge
    //         )
    //         Spacer(modifier = Modifier.width(16.dp))
    //         Text(selectedCategory, style = MaterialTheme.typography.titleMedium)
    //     }

    //     CustomDatePickerDialog(
    //         showDialog = showDateDialog,
    //         onDismiss = { showDateDialog = false },
    //         onDateSelected = {
    //             selectedMonth = it // 이미 "yyyy년 M월" 형식
    //         }
    //     )

    //     Spacer(modifier = Modifier.height(16.dp))

    //     Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    //         Text("사유", modifier = Modifier.weight(1f))
    //         Text("금액", modifier = Modifier.weight(1f))
    //         Text("날짜", modifier = Modifier.weight(1f))
    //     }

    //     Spacer(modifier = Modifier.height(8.dp))

    //     LazyColumn {
    //         items(expenseList) { item ->
    //             Row(
    //                 modifier = Modifier
    //                     .fillMaxWidth()
    //                     .padding(vertical = 6.dp),
    //                 horizontalArrangement = Arrangement.SpaceBetween
    //             ) {
    //                 Text(item.data.name, modifier = Modifier.weight(1f))
    //                 Text("${item.data.price}원", modifier = Modifier.weight(1f))
    //                 Text(item.data.date.toString(), modifier = Modifier.weight(1f))
    //             }
    //         }
    //     }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }
}
