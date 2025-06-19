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
import com.example.allaccountbook.database.entity.InvestEntity
import com.example.allaccountbook.database.model.InvestType
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.uiComponent.add.AddType
import com.example.allaccountbook.uiComponent.investment.InvestInfo
import com.example.allaccountbook.uiComponent.toYearMonth
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Locale


data class InvestInfo(
    val invName : String,
    var invCount : Int,
    var invPriceTotal : Int,
    val invAver: Int = invPriceTotal / invCount
)

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
                Text("평균 단가")
                Text("총액")
            } else {
                Text("투자 성향")
                Text("현재 보유율")
                Text("현재 보유량")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val nameStorage by remember(investList) {
            derivedStateOf {
                val tempList = mutableListOf<InvestInfo>()
                investList.forEach { invest ->
                    val name = invest.data.name
                    val type = invest.data.type
                    val count = invest.data.count
                    val price = count * invest.data.price

                    val existing = tempList.find { it.invName == name }
                    if (existing != null) {
                        if (type == InvestType.BUY) {
                            existing.invCount += count
                            existing.invPriceTotal += price
                        } else {
                            existing.invCount -= count
                            existing.invPriceTotal -= price
                            if(existing.invCount == 0){
                                tempList.remove(existing)

                            }
                        }
                    } else {
                        if (type == InvestType.BUY) {
                            tempList.add(InvestInfo(name, count, price))
                        }
                    }

                }
                tempList
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            if (viewMode == "전체") {
                nameStorage.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItemName = it.invName
                                showDialog = true
                            }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(it.invName)
                        Text(it.invCount.toString())
                        Text(it.invAver.toString())
                        Text(it.invPriceTotal.toString())
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


