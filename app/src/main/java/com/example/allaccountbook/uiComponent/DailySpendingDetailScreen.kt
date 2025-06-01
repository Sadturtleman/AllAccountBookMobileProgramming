package com.example.allaccountbook.uiComponent

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.allaccountbook.database.model.TransactionCategory
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.getAmount
import com.example.allaccountbook.database.model.getCategory
import com.example.allaccountbook.database.model.getDate
import com.example.allaccountbook.database.model.getName
import com.example.allaccountbook.uiPersistent.BottomNevBar
import com.example.allaccountbook.uiPersistent.formatWithCommas
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.lazy.items

@Composable
fun DailySpendingDetailScreen(
    selectedDate: String,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val allFilters = TransactionCategory.entries.map { it.label }
    val selectedFilters = remember { mutableStateListOf<String>() }

    var spendings by remember { mutableStateOf<List<TransactionDetail>>(emptyList()) }

    LaunchedEffect(selectedDate) {
        spendings = viewModel.getTransactionsByDate(selectedDate)
    }

    val parsedDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).parse(selectedDate)
    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(parsedDate!!)

    val filteredSpendings = if (selectedFilters.isEmpty()) {
        spendings.filter { it.getDate() == formattedDate }
    } else {
        spendings.filter {
            it.getDate() == formattedDate && selectedFilters.contains(it.getCategory())
        }
    }

    val totalAmount = filteredSpendings.sumOf { it.getAmount() }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(selectedDate, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "총 합계: ${formatWithCommas(totalAmount)}원",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
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

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("사유", fontWeight = FontWeight.Bold)
            Text("금액", fontWeight = FontWeight.Bold)
            Text("카테고리", fontWeight = FontWeight.Bold)
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredSpendings) { detail ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(detail.getName())
                    Text("${formatWithCommas(detail.getAmount())}원")
                    Text(detail.getCategory())
                }
            }
        }

        BottomNevBar()
    }
}

@Preview
@Composable
private fun DailySpendingDetailScreenPrev() {
    DailySpendingDetailScreen("2025년 6월 2일")
}