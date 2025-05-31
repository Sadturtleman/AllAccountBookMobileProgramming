package com.example.allaccountbook.uiComponent.Investment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.allaccountbook.uiPersistent.BottomNevBar
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog

@Composable
fun InvestmentDetailScreen(navController: NavController) {
    var selectedMonth by remember { mutableStateOf("2025-05") }
    var showDateDialog by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf("전체") }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showTrendDialog by remember { mutableStateOf(false) }

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
                    .clickable { showDateDialog = true }
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

        CustomDatePickerDialog(
            showDialog = showDateDialog,
            onDismiss = { showDateDialog = false },
            onDateSelected = {
                selectedMonth = it.substring(0, 7)
            }
        )

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
                repeat(3) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDialog = true }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("A주식")
                        Text("3")
                        Text("10,000")
                        Text("30,000")
                    }
                }
            } else {
                repeat(3) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTrendDialog = true }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("부동산")
                        Text("15,000")
                        Text("200,000")
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("총")
                    Text("45,000")
                    Text("600,000")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showDialog) {
            InvestmentSummaryDialog(
                onDismiss = { showDialog = false },
                navController = navController,
                stockName = "A주식"
            )
        }

        if (showTrendDialog) {
            InvestmentTrendDialog(
                onDismiss = { showTrendDialog = false },
                navController = navController,
                trendName = "부동산"
            )
        }

        BottomNevBar()
    }
}

@Preview(showBackground = true)
@Composable
fun InvestmentDetailScreenPreview() {
    val navController = rememberNavController()
    InvestmentDetailScreen(navController)
}
