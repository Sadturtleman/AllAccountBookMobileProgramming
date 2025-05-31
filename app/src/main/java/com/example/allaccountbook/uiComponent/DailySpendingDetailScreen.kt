package com.example.allaccountbook.uiComponent

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allaccountbook.uiPersistent.BottomNevBar
import com.example.allaccountbook.uiPersistent.formatWithCommas

@Composable
fun DailySpendingDetailScreen(
    selectedDate : String
) {
    // <추가> filter 예시 - 내가 등록한 카테고리, 카드 / 통장 있도록
    val allFilters = listOf("식음료", "잡화", "교통", "쇼핑", "기타", "건강", "교육", "문화", "기타1", "기타2")
    val selectedFilters = remember { mutableStateListOf<String>() }

    // <DB> 데이터 베이스에서 가져올 정보 (selectedDate의 날짜에 해당하는 것들 가져오기)
    val spendings = listOf(
        SpendingDetail("커피", 3000, "식음료", "카카오뱅크"),
        SpendingDetail("편의점", 2000, "잡화", "국민카드"),
        SpendingDetail("택시", 12000, "교통", "토스뱅크"),
        SpendingDetail("택시", 12000, "교통", "토스뱅크")
    )

    val filteredSpendings = if (selectedFilters.isEmpty()) spendings
    else spendings.filter { selectedFilters.contains(it.category) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(selectedDate, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Text("카테고리 필터:", fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allFilters.forEach { filter ->
                val isSelected = selectedFilters.contains(filter)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (isSelected) selectedFilters.remove(filter)
                        else selectedFilters.add(filter)
                    },
                    label = { Text(filter) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("사유", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text("사용 금액", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text("카테고리", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text("카드/통장", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
            ) {
            items(filteredSpendings) { spending ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(spending.reason)
                    Text("${formatWithCommas(spending.amount)}원")
                    Text(spending.category)
                    Text(spending.method)
                }
            }
        }
        BottomNevBar()
    }
}

data class SpendingDetail(
    val reason: String,
    val amount: Int,
    val category: String,
    val method: String
)

@Preview
@Composable
private fun DailySpendingDetailScreenPrev() {
    DailySpendingDetailScreen("2025년 6월 2일")
}