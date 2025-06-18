package com.example.allaccountbook.uiComponent.showDaily

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.getAmount
import com.example.allaccountbook.database.model.getDate
import com.example.allaccountbook.database.model.getCategory
import com.example.allaccountbook.uiComponent.model.YearMonthPickerDialog
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.uiPersistent.formatWithCommas
import com.example.allaccountbook.uiPersistent.showDate
import com.example.allaccountbook.viewmodel.view.BorrowViewModel
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun ShowDailyScreen(
    viewModel: TransactionViewModel = hiltViewModel(),
    borrowViewModel: BorrowViewModel = hiltViewModel(),
    navController: NavController
) {
    val usedCategories = remember { mutableStateListOf<String>() }
    val selectedCategories = remember { mutableStateListOf<String>() }
    val coroutineScope = rememberCoroutineScope()

    val initialPage = 60
    val pagerState = rememberPagerState(initialPage = initialPage)

    val currentDate by remember(pagerState.currentPage) {
        derivedStateOf {
            LocalDate.now().withDayOfMonth(1).plusMonths((pagerState.currentPage - initialPage).toLong())
        }
    }

    val selectedMonth = currentDate.format(DateTimeFormatter.ofPattern("yyyy년 M월", Locale.KOREA))
    var showDateDialog by remember { mutableStateOf(false) }
    val transactions by viewModel.transactions.collectAsState()
    val borrowList by borrowViewModel.borrowList.collectAsState()

    LaunchedEffect(currentDate, transactions) {
        val currentMonthCategories = transactions
            .filter {
                val date = LocalDate.parse(it.getDate())
                date.year == currentDate.year && date.month == currentDate.month
            }
            .map { it.getCategory() }
            .distinct()

        usedCategories.clear()
        usedCategories.addAll(currentMonthCategories)
        selectedCategories.retainAll(usedCategories)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
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
            YearMonthPickerDialog(
                showDialog = showDateDialog,
                onDismiss = { showDateDialog = false },
                onDateSelected = { newMonth ->
                    val parsed = YearMonth.parse(newMonth, DateTimeFormatter.ofPattern("yyyy년 M월", Locale.KOREA)).atDay(1)
                    val diff = ChronoUnit.MONTHS.between(LocalDate.now().withDayOfMonth(1), parsed)
                    coroutineScope.launch {
                        pagerState.scrollToPage(initialPage + diff.toInt())
                    }
                    showDateDialog = false
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            usedCategories.forEach { category ->
                FilterChip(
                    selected = selectedCategories.contains(category),
                    onClick = {
                        if (selectedCategories.contains(category)) selectedCategories.remove(category)
                        else selectedCategories.add(category)
                    },
                    label = { Text(category) }
                )
            }
        }

        HorizontalPager(
            count = 120,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val pageDate = LocalDate.now().withDayOfMonth(1).plusMonths((page - initialPage).toLong())
            ShowMonthlyCalendar(
                parsedDate = pageDate,
                transactions = transactions,
                borrowList = borrowList,
                selectedCategories = selectedCategories,
                navController = navController
            )
        }

        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }
}

@Composable
fun ShowMonthlyCalendar(
    parsedDate: LocalDate,
    transactions: List<TransactionDetail>,
    borrowList: List<com.example.allaccountbook.model.BorrowMoney>,
    selectedCategories: List<String>,
    navController: NavController
) {
    val yearMonth = YearMonth.from(parsedDate)
    val daysInMonth = yearMonth.lengthOfMonth()
    val startDayOfWeek = parsedDate.dayOfWeek.value % 7
    val filteredData = transactions.filter { detail ->
        val date = LocalDate.parse(detail.getDate())
        val category = detail.getCategory()
        (selectedCategories.isEmpty() || category in selectedCategories) &&
                date.year == parsedDate.year &&
                date.month == parsedDate.month
    }

    val calendarRows = buildCalendarGrid(startDayOfWeek, daysInMonth)
    var showChoiceDialog by remember { mutableStateOf(false) }
    var clickedDate by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Text(
                    text = it,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        calendarRows.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(2.dp)
                            .background(Color(0xFFF5F5F5), MaterialTheme.shapes.small)
                            .let { mod ->
                                if (day != null) {
                                    mod.clickable {
                                        val dayStr = day.toString().padStart(2, '0')
                                        val fullDate = "${parsedDate.year}-${parsedDate.monthValue.toString().padStart(2, '0')}-$dayStr"

                                        val dailyTransactions = filteredData.filter { it.getDate() == fullDate }
                                        val totalExpense = dailyTransactions.filter { it.type.name == "EXPENSE" }.sumOf { it.getAmount() }
                                        val totalIncome = dailyTransactions.filter { it.type.name == "INCOME" }.sumOf { it.getAmount() }

                                        val dailyBorrows = borrowList.filter {
                                            SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(it.date) == fullDate
                                        }
                                        val totalBorrow = dailyBorrows.filter { it.type.name == "BORROW" }.sumOf { it.price }
                                        val totalBorrowed = dailyBorrows.filter { it.type.name == "BORROWED" }.sumOf { it.price }

                                        val hasSpend = totalExpense > 0 || totalIncome > 0
                                        val hasBorrow = totalBorrow > 0 || totalBorrowed > 0

                                        when {
                                            hasSpend && hasBorrow -> {
                                                clickedDate = fullDate
                                                showChoiceDialog = true
                                            }
                                            hasSpend -> navController.navigate("dailyDetail/$fullDate")
                                            hasBorrow -> navController.navigate("lendBorrowList/$fullDate")
                                        }
                                    }
                                } else mod
                            },
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (day != null) {
                            val fullDate = "${parsedDate.year}-${parsedDate.monthValue.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
                            val dailyTransactions = filteredData.filter { it.getDate() == fullDate }
                            val totalExpense = dailyTransactions.filter { it.type.name == "EXPENSE" }.sumOf { it.getAmount() }
                            val totalIncome = dailyTransactions.filter { it.type.name == "INCOME" }.sumOf { it.getAmount() }

                            val dailyBorrows = borrowList.filter {
                                SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(it.date) == fullDate
                            }
                            val totalBorrow = dailyBorrows.filter { it.type.name == "BORROW" }.sumOf { it.price }
                            val totalBorrowed = dailyBorrows.filter { it.type.name == "BORROWED" }.sumOf { it.price }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$day", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(4.dp))
                                if (totalExpense > 0) Text("${formatWithCommas(totalExpense)}원", fontSize = 12.sp, color = Color.Red)
                                if (totalIncome > 0) Text("${formatWithCommas(totalIncome)}원", fontSize = 12.sp, color = Color.Blue)
                                if (totalBorrow > 0) Text("${formatWithCommas(totalBorrow)}원", fontSize = 12.sp, color = Color(0xFF6C3483))
                                if (totalBorrowed > 0) Text("${formatWithCommas(totalBorrowed)}원", fontSize = 12.sp, color = Color(0xFF2874A6))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showChoiceDialog) {
        AlertDialog(
            onDismissRequest = { showChoiceDialog = false },
            title = { Text("어떤 내역을 확인하시겠어요?") },
            text = { Text("이 날짜는 수입/지출 내역과 빌림/빌려준 내역이 모두 있습니다.") },
            confirmButton = {
                Button(onClick = {
                    navController.navigate("dailyDetail/$clickedDate")
                    showChoiceDialog = false
                }) { Text("수입/지출 보기") }
            },
            dismissButton = {
                Button(onClick = {
                    navController.navigate("lendBorrowList/$clickedDate")
                    showChoiceDialog = false
                }) { Text("빌림/빌려준 보기") }
            }
        )
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
