package com.example.allaccountbook.uiComponent.investment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.allaccountbook.database.model.InvestType
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.uiComponent.toYearMonth
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

data class InvestInfo(
    val invName: String,
    var invCount: Int,
    var invPriceTotal: Int,
    val invCategory: String
) {
    val invAver: Int
        get() = if (invCount > 0) invPriceTotal / invCount else 0
}

data class InvestTrendInfo(
    val trendName: String,
    var trandTotalPrice: Int
)

@Composable
fun InvestmentDetailScreen(
    navController: NavController,
    selectedDate: String,
    viewmodel: TransactionViewModel = hiltViewModel()
) {
    var selectedMonth by remember { mutableStateOf(selectedDate) }
    val selectedDateFormat = remember { SimpleDateFormat("yyyy년 M월", Locale.KOREA) }
    val selectedDateObj = remember(selectedDate) { selectedDateFormat.parse(selectedDate) }
    val selectedYearMonth = selectedDateObj?.toYearMonth()

    var viewMode by remember { mutableStateOf("전체") }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showTrendDialog by remember { mutableStateOf(false) }
    var total by remember { mutableStateOf(0) }

    val transactions = viewmodel.transactions.collectAsState()
    val formatter = remember { NumberFormat.getNumberInstance(Locale.KOREA) }

    val investList by remember(transactions.value, selectedYearMonth) {
        derivedStateOf {
            transactions.value
                .filterIsInstance<TransactionDetail.Invest>()
                .filter {
                    selectedYearMonth != null && it.data.date.toYearMonth() == selectedYearMonth
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
            OutlinedButton(
                onClick = { /* optional calendar */ },
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(text = selectedMonth, fontSize = 15.sp)
            }

            Box {
                Button(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (viewMode == "전체") "투자 전체 보기 ▼" else "투자 성향별 보기 ▼",
                        fontSize = 14.sp
                    )
                }

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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            if (viewMode == "전체") {
                Text("소유 종목", modifier = Modifier.weight(1f), textAlign = TextAlign.Start, fontWeight = FontWeight.Bold)
                Text("평균 단가", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                Text("총액", modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.Bold)
            } else {
                Text("투자 성향", modifier = Modifier.weight(1f), textAlign = TextAlign.Start, fontWeight = FontWeight.Bold)
                Text("현재 보유율", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                Text("현재 보유량", modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.Bold)
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
                    val category = invest.data.category

                    val existing = tempList.find { it.invName == name }
                    if (existing != null) {
                        if (type == InvestType.BUY) {
                            existing.invCount += count
                            existing.invPriceTotal += price
                        } else {
                            existing.invPriceTotal -= count * (existing.invPriceTotal / existing.invCount)
                            existing.invCount -= count
                            if (existing.invCount == 0) tempList.remove(existing)
                        }
                    } else {
                        if (type == InvestType.BUY) {
                            tempList.add(InvestInfo(name, count, price, invCategory = category))
                        }
                    }
                }
                tempList
            }
        }

        val categoryStorage by remember(investList) {
            derivedStateOf {
                val tempList = mutableListOf<InvestTrendInfo>()

                nameStorage.forEach { invest ->
                    val category = invest.invCategory
                    val TotalPrice = invest.invPriceTotal
                    val existing = tempList.find { it.trendName == category }
                    if (existing != null) {
                        existing.trandTotalPrice += TotalPrice
                        total += TotalPrice
                    } else {
                        total += TotalPrice
                        tempList.add(InvestTrendInfo(category,TotalPrice))
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
                            .height(56.dp)
                            .clickable {
                                selectedItemName = it.invName
                                showDialog = true
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(it.invName)
                            Text("x${it.invCount}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(if (it.invCount > 0) "${formatter.format(it.invAver)}원" else "-")
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.End
                        ) {
                            Text("${formatter.format(it.invPriceTotal)}원")
                        }
                    }
                }
            } else {
                categoryStorage.forEach { categoryList ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItemCategory = categoryList.trendName
                                showTrendDialog = true
                            }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val trandHoldPercent = if(total == 0) 0 else (categoryList.trandTotalPrice * 100) / total
                        Text(categoryList.trendName)
                        Text("$trandHoldPercent %", textAlign = TextAlign.End)
                        Text("${formatter.format(categoryList.trandTotalPrice)}원", textAlign = TextAlign.End)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("총", fontWeight = FontWeight.Bold)
                    Text("-", fontWeight = FontWeight.Bold)
                    Text("${formatter.format(categoryStorage.sumOf { it.trandTotalPrice })}원", fontWeight = FontWeight.Bold)
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
