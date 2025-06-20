package com.example.allaccountbook.uiComponent.showDaily

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.getAmount
import com.example.allaccountbook.database.model.getCategory
import com.example.allaccountbook.database.model.getDate
import com.example.allaccountbook.database.model.getName
import com.example.allaccountbook.uiPersistent.formatWithCommas
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import com.example.allaccountbook.database.model.getLocalDate
import com.example.allaccountbook.uiPersistent.BottomNavBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
@Composable
fun DailySpendingDetailScreen(
    selectedDate: String,
    viewModel: TransactionViewModel = hiltViewModel(),
    navController: NavController
) {
    val transactions by viewModel.transactions.collectAsState()

    val parsedDate = remember(selectedDate) {
        LocalDate.parse(
            selectedDate,
            DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREA)
        )
    }

    val spendings = transactions.filter { it.getLocalDate() == parsedDate }

    val allFilters = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        val categories = viewModel.getAllCategories()
        allFilters.clear()
        allFilters.addAll(categories)
    }

    val selectedFilters = remember { mutableStateListOf<String>() }

    // ✅ 탭 상태 추가 (전체, 지출, 수입)
    val tabs = listOf("전체", "지출", "수입")
    var selectedTab by remember { mutableStateOf("전체") }

    // ✅ 탭 + 카테고리 필터 통합
    val filteredSpendings = spendings.filter {
        val matchesType = when (selectedTab) {
            "수입" -> it is TransactionDetail.Income
            "지출" -> it is TransactionDetail.Expense
            else -> true
        }
        val matchesCategory = selectedFilters.isEmpty() || selectedFilters.contains(it.getCategory())
        matchesType && matchesCategory
    }

    val totalAmount = filteredSpendings.sumOf { it.getAmount() }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(selectedDate, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ 탭 UI 추가
        TabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = { Text(tab) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "총 합계: ${formatWithCommas(totalAmount)}원",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

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

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

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

        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }
}
