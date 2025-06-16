package com.example.allaccountbook.uiComponent.investment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.uiComponent.toYearMonth
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun InvestmentDetailScreen(navController: NavController, selectedDate : String, viewmodel : TransactionViewModel = hiltViewModel()) {
    var selectedMonth by remember { mutableStateOf(selectedDate) }
    val selectedDateFormat = remember { SimpleDateFormat("yyyy년 M월", Locale.KOREA) }
    val selectedDateObj = remember(selectedDate) {
        selectedDateFormat.parse(selectedDate)
    }
    val selectedYearMonth = selectedDateObj?.toYearMonth()

    var viewMode by remember { mutableStateOf("전체") }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showTrendDialog by remember { mutableStateOf(false) }

    val transactions = viewmodel.transactions.collectAsState()
// 1. 투자 항목 필터링 (선택 월 기준)
    val investList by remember(transactions.value, selectedYearMonth) {
        derivedStateOf {
            transactions.value
                .filterIsInstance<TransactionDetail.Invest>()
                .filter {
                    selectedYearMonth != null &&
                            it.data.date.toYearMonth() == selectedYearMonth
                }
        }
    }

// 2. 카테고리별로 그룹화
    val investByCategory by remember(investList) {
        derivedStateOf {
            investList.groupBy { it.data.category }
        }
    }

// 3. 각 카테고리의 총 개수 및 총 금액 계산
    val categoryTotal by remember(investByCategory) {
        derivedStateOf {
            investByCategory.mapValues { (_, list) ->
                val totalCount = list.sumOf { it.data.count }
                val totalPrice = list.sumOf { it.data.count * it.data.price }
                totalCount to totalPrice
            }
        }
    }

    var selectedItemName by remember { mutableStateOf<String?>(null) }
    var selectedItemCategory by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedMonth,
                modifier = Modifier
                    .background(Color.LightGray)
                    .padding(8.dp)
            )

            Box {
                Text(
                    text = if (viewMode == "전체") "투자 전체 보기 ▼" else "투자 성향별 보기 ▼",
                    modifier = Modifier
                        .background(Color.LightGray)
                        .clickable { expanded = true }
                        .padding(8.dp)
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("투자 전체 보기") },
                        onClick = {
                            viewMode = "전체"
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("투자 성향별 보기") },
                        onClick = {
                            viewMode = "성향"
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (viewMode == "전체") {
                Text("소유 종목")
                Text("소유 개수")
                Text("평균단가")
                Text("총액")
            } else {
                Text("투자 성향")
                Text("월별 손익")
                Text("현재 보유량")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            if (viewMode == "전체") {
                investList.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItemName = it.data.name
                                showDialog = true
                            }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(it.data.name)
                        Text(it.data.count.toString())
                        Text(it.data.price.toString())
                        Text((it.data.count * it.data.price).toString())
                    }
                }
            } else {

                categoryTotal.forEach { (category, pair) ->
                    val (count, price) = pair
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItemCategory = category
                                showTrendDialog = true // 필요시 category 이름도 넘길 수 있음
                            }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(category)
                        Text(count.toString())
                        Text(price.toString())
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                val totalCount = categoryTotal.values.sumOf { it.first }
                val totalPrice = categoryTotal.values.sumOf { it.second }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("총")
                    Text(totalCount.toString())
                    Text(totalPrice.toString())
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showDialog) {
            navController.navigate("investmentSummaryDetail/${selectedDate}/${selectedItemName}")
        }

        if (showTrendDialog) {
            navController.navigate("investmentTrendDetail/$selectedDate/$selectedItemCategory")
        }

        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }
}
