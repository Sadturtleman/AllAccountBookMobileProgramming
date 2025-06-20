package com.example.allaccountbook.uiComponent.lendBorrow


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.allaccountbook.uiPersistent.showDate
import com.example.allaccountbook.viewmodel.view.BorrowViewModel
import com.example.allaccountbook.model.BorrowType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.allaccountbook.uiComponent.toYearMonth
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.uiPersistent.formatWithCommas


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LendBorrowListScreen(
    selectedDate: String,
    viewModel: BorrowViewModel = hiltViewModel(),
//    onAddClick: () -> Unit = {}, // 👈 새로 추가할 항목 입력 화면 이동용 콜백(AddBorrowItemScreen으로 이동 필요)
    navController : NavController
) {
    val typeOptions = listOf("빌려준 목록", "빌린 목록")
    var selectedType by remember { mutableStateOf(typeOptions[0]) }
    var showUnpaidOnly by remember { mutableStateOf(false) }
    val selectedDateFormat = remember { SimpleDateFormat("yyyy년 M월", Locale.KOREA) }
    val selectedDateObj = selectedDateFormat.parse(selectedDate)

    val borrowList by viewModel.borrowList.collectAsState()

    val filteredData = borrowList.filter {
        it.date.toYearMonth() == selectedDateObj?.toYearMonth()
        }
        .filter {
        val typeMatch = when (selectedType) {
            "빌려준 목록" -> it.type == BorrowType.BORROWED
            "빌린 목록" -> it.type == BorrowType.BORROW
            else -> true
        }
        val unpaidMatch = if (showUnpaidOnly) !it.finished else true
        typeMatch && unpaidMatch
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addBorrow")
            }) {
                Text("+") // 혹은 Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        bottomBar = { BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        ) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row {
                showDate(selectedDate)
                var expanded by remember { mutableStateOf(false) }
                Spacer(modifier = Modifier.width(100.dp))
                Box(modifier = Modifier.height(28.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = selectedType,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            typeOptions.forEach { option ->
                                DropdownMenuItem(text = { Text(option) }, onClick = {
                                    selectedType = option
                                    expanded = false
                                })
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = showUnpaidOnly, onCheckedChange = { showUnpaidOnly = it })
                Text("미완료만 보기")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "현재 목록: $selectedType",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("사유", fontSize = 16.sp)
                Text("금액", fontSize = 16.sp)
                Text("대상", fontSize = 16.sp)
                Text("날짜", fontSize = 16.sp)
                Text("완료여부", fontSize = 16.sp)
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(filteredData) { _, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val textStyle = if (item.finished)
                            TextStyle(textDecoration = TextDecoration.LineThrough)
                        else TextStyle.Default

                        Text(style = textStyle, text = item.reason)
                        Text(style = textStyle, text = formatWithCommas(item.price))
                        Text(style = textStyle, text = item.person)
                        Text(style = textStyle, text = item.date.formatToString())
                        Checkbox(
                            checked = item.finished,
                            onCheckedChange = {
                                val updated = item.copy(finished = it)
                                viewModel.updateBorrow(updated)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { viewModel.resetAndInsertDummyData() }) {
                Text("DB 초기화 + 더미 삽입")
            }
        }
    }
}


// 날짜 포맷 확장 함수
fun Date.formatToString(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(this)
}

@Preview
@Composable
private fun LendBorrowPrev() {
//    LendBorrowListScreen(
//        "2025년 05월",
//        onAddClick = {}
//    )
}
