package com.example.allaccountbook.uiComponent

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.model.BorrowType
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog
import com.example.allaccountbook.uiPersistent.formatWithCommas
import com.example.allaccountbook.uiPersistent.showDate
import com.example.allaccountbook.viewmodel.view.BorrowViewModel
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewmodel: TransactionViewModel = hiltViewModel(),
    borrowViewModel: BorrowViewModel = hiltViewModel(),
    navController: NavController
) {

    var selectedDate by remember { mutableStateOf("2025년 5월") }
    var showDateDialog by remember { mutableStateOf(false) }

    val selectedDateFormat = remember { SimpleDateFormat("yyyy년 M월", Locale.KOREA) }
    val selectedDateObj = remember(selectedDate) {
        selectedDateFormat.parse(selectedDate)
    }
    val selectedYearMonth = selectedDateObj?.toYearMonth()


    val transactions by viewmodel.transactions.collectAsState()

    val borrow by borrowViewModel.borrowList.collectAsState()
    borrow.forEach {
        Log.d("borrow", it.toString())
    }
    val savingList = transactions
        .filterIsInstance<TransactionDetail.Saving>()
        .filter {
            val start = it.data.startDate.toYearMonth()
            val end = it.data.endDate.toYearMonth()
            selectedYearMonth != null && isYearMonthInRange(selectedYearMonth, start, end)
        }


    val expenseList = transactions
        .filterIsInstance<TransactionDetail.Expense>()
        .filter {
            selectedYearMonth != null &&
                    it.data.date.toYearMonth() == selectedYearMonth
        }

    val incomeList = transactions
        .filterIsInstance<TransactionDetail.Income>()
        .filter {
            selectedYearMonth != null &&
                    it.data.date.toYearMonth() == selectedYearMonth
        }

    val borrowList = borrow.filter {
        it.type == BorrowType.BORROW &&
                selectedYearMonth != null &&
                it.date.toYearMonth() == selectedYearMonth
    }

    val lendList = borrow.filter {
        it.type == BorrowType.BORROWED &&
                selectedYearMonth != null &&
                it.date.toYearMonth() == selectedYearMonth
    }

    var usagePercent by remember {
        mutableFloatStateOf(
            (expenseList.sumOf { it.data.price }.toFloat()).let { expense ->
                val income = incomeList.sumOf { it.data.price }
                if (income == 0) 0f else expense / income
            }
        )
    }

    var getTotalSavings by remember { mutableIntStateOf(savingList.sumOf { it.data.price }) }
    var getTotalInvestments by remember { mutableIntStateOf(3332000) }
    var getAvailableBalance by remember { mutableIntStateOf(incomeList.sumOf { it.data.price }) }

    val getTotalAmount by remember {
        derivedStateOf {
            getTotalSavings + getTotalInvestments + getAvailableBalance
        }
    }

    val getTotalLentAmount by remember(lendList) {
        derivedStateOf {
            lendList.sumOf { it.price }
        }
    }
    val getTotalBorrowedAmount by remember(borrowList) {
        derivedStateOf {
            borrowList.sumOf { it.price }
        }
    }

    Log.d("TotalBorrow", getTotalLentAmount.toString())
    Log.d("TotalBorrowed", getTotalBorrowedAmount.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // 날짜 및 선택
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                showDate(selectedDate)
                Button(onClick = { showDateDialog = true }) {
                    Text("날짜 선택")
                }
            }


            CustomDatePickerDialog(
                showDialog = showDateDialog,
                onDismiss = { showDateDialog = false },
                onDateSelected = { selectedDate = it }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow(
                    label = "전체 총 금액",
                    value = formatWithCommas(getTotalAmount),
                    fontSize = 25,
                    modifier = Modifier.clickable { /* 화면 이동 */ }
                )

                InfoRow(
                    label = "저축 총계",
                    value = formatWithCommas(getTotalSavings),
                    modifier = Modifier.clickable {
                        navController.navigate("savingDetail")
                    }
                )


                InfoRow(
                    label = "투자 총계",
                    value = formatWithCommas(getTotalInvestments),
                    modifier = Modifier.clickable { navController.navigate("investmentDetail")/* 화면 이동 */ }
                )

                InfoRow(
                    label = "사용 가능 금액",
                    value = formatWithCommas(getAvailableBalance),
                    modifier = Modifier.clickable { navController.navigate("availableDetail")/* 화면 이동 */ }
                )

                InfoRow(
                    label = "빌린 전체 금액",
                    value = formatWithCommas(getTotalLentAmount),
                    modifier = Modifier.clickable { navController.navigate("lendBorrowList/${selectedDate}") }
                )

                InfoRow(
                    label = "빌려준 금액",
                    value = formatWithCommas(getTotalBorrowedAmount),
                    modifier = Modifier.clickable { navController.navigate("lendBorrowList/${selectedDate}") }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = usagePercent / 100f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFFF88), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "남은 사용률 : ${usagePercent.toInt()}%",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate("addBorrow")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+추가하기")
                }
                Button(
                    onClick = {
                        navController.navigate("phoneRegister")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("전화번호 등록")
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


@Composable
fun InfoRow(
    label: String,
    value: String,
    fontSize: Int = 20,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = fontSize.sp, fontWeight = FontWeight.SemiBold)
        Text(value, fontSize = fontSize.sp)
    }
}

fun Date.toYearMonth(): Pair<Int, Int> {
    val cal = Calendar.getInstance().apply { time = this@toYearMonth }
    return cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH) // 0-based month
}

fun isYearMonthInRange(
    target: Pair<Int, Int>,
    start: Pair<Int, Int>,
    end: Pair<Int, Int>
): Boolean {
    return (target.first > start.first || (target.first == start.first && target.second >= start.second)) &&
            (target.first < end.first || (target.first == end.first && target.second <= end.second))
}