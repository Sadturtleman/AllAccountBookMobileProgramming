package com.example.allaccountbook.uiComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allaccountbook.uiPersistent.BottomNevBar
import com.example.allaccountbook.uiPersistent.formatWithCommas
import com.example.allaccountbook.uiPersistent.showDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDailyScreen() {
    var selectedMonth by remember { mutableStateOf("2025년 05월") }
    var showDateDialog by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월", Locale.KOREA)
    val parsedDate = YearMonth.parse(selectedMonth, dateFormatter).atDay(1)
    val yearMonth = YearMonth.from(parsedDate)
    val daysInMonth = yearMonth.lengthOfMonth()
    val startDayOfWeek = parsedDate.dayOfWeek.value % 7 // 일요일 = 0

    // 예시 카테고리
    val categoryOptions = listOf("식비", "교통", "쇼핑", "의료", "여가", "기타")
    // 예시 카드 / 통장
    val categoryCardOptions = listOf("카카오 페이", "현금","학생증")
    val selectedCategories = remember { mutableStateListOf<String>() }
    val selectedCards = remember{mutableStateListOf<String>()}

    // 예시 데이터
    val dailyData = listOf(
        DaySpending("2025-05-01", 32000, "식비", "카카오 페이"),
        DaySpending("2025-05-01", 33000, "교통", "현금"),
        DaySpending("2025-05-02", 8000, "교통", "현금"),
        DaySpending("2025-05-03", 15000, "쇼핑", "학생증"),
        DaySpending("2025-05-08", 12000, "의료", "카카오 페이"),
        DaySpending("2025-05-14", 9000, "여가", "학생증"),
        DaySpending("2025-05-20", 5000, "기타", "카카오 페이")
    )

    val filteredData = dailyData.filter { data ->
        val itemDate = LocalDate.parse(data.date)
        itemDate.year == parsedDate.year && itemDate.month == parsedDate.month &&
                (selectedCategories.isEmpty() || selectedCategories.contains(data.category)) &&
                (selectedCards.isEmpty() || selectedCards.contains(data.card))
    }

    val calendarRows = buildCalendarGrid(startDayOfWeek, daysInMonth)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            showDate(selectedMonth)
            Button(onClick = { showDateDialog = true }) {
                Text("날짜 선택")
            }
        }

        if (showDateDialog) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDateDialog = false },
                confirmButton = {
                    OutlinedButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val date = Date(millis)
                            val format = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
                            selectedMonth = format.format(date)
                        }
                        showDateDialog = false
                    }) {
                        Text("확인")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDateDialog = false }) {
                        Text("취소")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Column {
            Text("📌 카테고리 필터 : ")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoryOptions.forEach { category ->
                    FilterChip(
                        selected = selectedCategories.contains(category),
                        onClick = {
                            if (selectedCategories.contains(category)) {
                                selectedCategories.remove(category)
                            } else {
                                selectedCategories.add(category)
                            }
                        },
                        label = { Text(category) }
                    )
                }
            }
            Text("📌 카드 / 통장 필터 : ")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoryCardOptions.forEach { category ->
                    FilterChip(
                        selected = selectedCards.contains(category),
                        onClick = {
                            if (selectedCards.contains(category)) {
                                selectedCards.remove(category)
                            } else {
                                selectedCards.add(category)
                            }
                        },
                        label = { Text(category) }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(12.dp))



        Column(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState())
        ) {
            // 요일 헤더
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 날짜 셀
            calendarRows.forEach { week ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    week.forEach { day ->
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color(0xFFF5F5F5), MaterialTheme.shapes.small),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            if (day != null) {
                                val dayStr = String.format("%02d", day)
                                val fullDate = "${parsedDate.year}-${parsedDate.monthValue.toString().padStart(2, '0')}-$dayStr"
                                val totalAmount = filteredData
                                    .filter { it.date == fullDate }
                                    .sumOf { it.amount }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("$day", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(4.dp))
                                    if (totalAmount > 0) {
                                        Text(
                                            text = "${formatWithCommas(totalAmount)}원",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        BottomNevBar()
    }
}

fun buildCalendarGrid(startDayOfWeek: Int, totalDays: Int): List<List<Int?>> {
    val grid = mutableListOf<List<Int?>>()
    var currentDay = 1
    val firstWeek = MutableList(7) { index -> if (index >= startDayOfWeek) currentDay++ else null }
    grid.add(firstWeek)
    while (currentDay <= totalDays) {
        val week = MutableList(7) {
            if (currentDay <= totalDays) currentDay++ else null
        }
        grid.add(week)
    }
    return grid
}

data class DaySpending(val date: String, val amount: Int, val category: String, val card : String)

@Preview
@Composable
fun ShowDailyScreenPreview() {
    ShowDailyScreen()
}
