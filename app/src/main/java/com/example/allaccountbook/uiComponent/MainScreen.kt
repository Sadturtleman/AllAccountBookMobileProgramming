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
import com.example.allaccountbook.model.BorrowType
import com.example.allaccountbook.uiComponent.model.YearMonthPickerDialog
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog
import com.example.allaccountbook.uiPersistent.formatWithCommas
import com.example.allaccountbook.uiPersistent.showDate
import com.example.allaccountbook.viewmodel.view.BorrowViewModel
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.time.ZoneId
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewmodel: TransactionViewModel = hiltViewModel(),
    borrowViewModel: BorrowViewModel = hiltViewModel(),
    navController: NavController
) {
    val currentMonthDefault = remember {
        SimpleDateFormat("yyyy-MM", Locale.KOREA).format(Date())
    }

    var selectedDate by rememberSaveable { mutableStateOf(currentMonthDefault) }
    var showDateDialog by remember { mutableStateOf(false) }

    val selectedYearMonth = remember(selectedDate) {
        YearMonth.parse(selectedDate)
    }

    val transactions by viewmodel.transactions.collectAsState()
    val borrow by borrowViewModel.borrowList.collectAsState()

    val savingList = transactions.filterIsInstance<TransactionDetail.Saving>().filter {
        val start = it.data.startDate.toYearMonth()
        val end = it.data.endDate.toYearMonth()
        selectedYearMonth in start..end
    }

    val expenseList = transactions.filterIsInstance<TransactionDetail.Expense>().filter {
        it.data.date.toYearMonth() == selectedYearMonth
    }

    val incomeList = transactions.filterIsInstance<TransactionDetail.Income>().filter {
        it.data.date.toYearMonth() == selectedYearMonth
    }

    val borrowList = borrow.filter {
        it.type == BorrowType.BORROW && it.date.toYearMonth() == selectedYearMonth
    }

    val lendList = borrow.filter {
        it.type == BorrowType.BORROWED && it.date.toYearMonth() == selectedYearMonth
    }

    val investList = transactions.filterIsInstance<TransactionDetail.Invest>().filter {
        it.data.date.toYearMonth() == selectedYearMonth
    }

    val usagePercent by remember(expenseList, incomeList) {
        derivedStateOf {
            val expense = expenseList.sumOf { it.data.price }.toFloat()
            val income = incomeList.sumOf { it.data.price }
            if (income == 0) 0f else expense / income * 100
        }
    }

    val getTotalSavings by remember(savingList) {
        derivedStateOf { savingList.sumOf { it.data.price } }
    }

    val getAvailableBalance by remember(incomeList) {
        derivedStateOf { incomeList.sumOf { it.data.price } - expenseList.sumOf { it.data.price }}
    }

    val getTotalInvestments by remember(investList) {
        derivedStateOf {
            investList.sumOf { it.data.price * it.data.count }
        }
    }

    val getTotalAmount by remember(getTotalSavings, getTotalInvestments, getAvailableBalance) {
        derivedStateOf {
            getTotalSavings + getTotalInvestments + getAvailableBalance
        }
    }

    val getTotalLentAmount by remember(lendList) {
        derivedStateOf { lendList.sumOf { it.price } }
    }

    val getTotalBorrowedAmount by remember(borrowList) {
        derivedStateOf { borrowList.sumOf { it.price } }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
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

            YearMonthPickerDialog(
                showDialog = showDateDialog,
                onDismiss = { showDateDialog = false },
                onDateSelected = {
                    val parsed = SimpleDateFormat("yyyy년 M월", Locale.KOREA).parse(it)
                    selectedDate = SimpleDateFormat("yyyy-MM", Locale.KOREA).format(parsed)
                }
            )

            Column(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp)).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow("전체 총 금액", formatWithCommas(getTotalAmount), fontSize = 25)
                InfoRow("저축 총계", formatWithCommas(getTotalSavings)) {
                    navController.navigate("savingDetail")
                }
                InfoRow("투자 총계", formatWithCommas(getTotalInvestments)) {
                navController.navigate("investmentDetail/${formatDateToDisplay(selectedDate)}")
            }
                InfoRow("사용 가능 금액", formatWithCommas(getAvailableBalance)) {
                    navController.navigate("availableDetail/${formatDateToDisplay(selectedDate)}")
                }
                InfoRow("빌린 전체 금액", formatWithCommas(getTotalLentAmount)) {
                    navController.navigate("lendBorrowList/${formatDateToDisplay(selectedDate)}")
                }
                InfoRow("빌려준 금액", formatWithCommas(getTotalBorrowedAmount)) {
                    navController.navigate("lendBorrowList/${formatDateToDisplay(selectedDate)}")
                }

            }

            Column(
                modifier = Modifier.fillMaxWidth().height(30.dp).background(Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(fraction = usagePercent / 100f).fillMaxHeight().background(Color(0xFFFFFF88), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text("남은 사용률 : ${usagePercent.toInt()}%", modifier = Modifier.padding(start = 8.dp), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { navController.navigate("addBorrow") }, modifier = Modifier.weight(1f)) {
                    Text("+추가하기")
                }
                Button(onClick = { navController.navigate("phoneRegister") }, modifier = Modifier.weight(1f)) {
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
fun InfoRow(label: String, value: String, fontSize: Int = 20, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Row(
        modifier = modifier.fillMaxWidth().clickable(enabled = onClick != null) { onClick?.invoke() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = fontSize.sp, fontWeight = FontWeight.SemiBold)
        Text(value, fontSize = fontSize.sp)
    }
}

fun Date.toYearMonth(): YearMonth {
    return this.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .let { YearMonth.of(it.year, it.month) }
}

fun formatDateToDisplay(date: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM", Locale.KOREA)
        val formatter = SimpleDateFormat("yyyy년 M월", Locale.KOREA)
        formatter.format(parser.parse(date)!!)
    } catch (e: Exception) {
        date // 예외 시 원본 반환
    }
}
