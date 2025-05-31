package com.example.allaccountbook.uiComponent


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.allaccountbook.uiPersistent.BottomNevBar
import com.example.allaccountbook.uiPersistent.showDate
import com.example.allaccountbook.viewmodel.view.BorrowViewModel
import com.example.allaccountbook.model.BorrowType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LendBorrowListScreen(
    selectedDate: String,
    viewModel: BorrowViewModel = hiltViewModel()
) {
    val typeOptions = listOf("빌려준 목록", "빌린 목록")
    var selectedType by remember { mutableStateOf(typeOptions[0]) }
    var showUnpaidOnly by remember { mutableStateOf(false) }

    val borrowList by viewModel.borrowList.collectAsState()

    // 선택된 목록과 미완료 조건을 적용한 필터링
    val filteredData = borrowList
        .filter {
            val typeMatch = when (selectedType) {
                "빌려준 목록" -> it.type == BorrowType.BORROWED
                "빌린 목록" -> it.type == BorrowType.BORROW
                else -> true
            }
            val unpaidMatch = if (showUnpaidOnly) !it.finished else true
            typeMatch && unpaidMatch
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 날짜 + 드롭다운
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

        // 미완료만 보기 체크박스
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = showUnpaidOnly, onCheckedChange = { showUnpaidOnly = it })
            Text("미완료만 보기")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 👇 현재 선택된 목록 표시
        Text(
            text = "📌 현재 목록: $selectedType",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 테이블 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("사유", fontSize = 16.sp)
            Text("대상", fontSize = 16.sp)
            Text("날짜", fontSize = 16.sp)
            Text("완료여부", fontSize = 16.sp)
        }

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // 본문 목록
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(filteredData) { _, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.reason)
                    Text(item.person)
                    Text(item.date.formatToString())
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

        // DB 초기화 버튼
        Button(onClick = { viewModel.resetAndInsertDummyData() }) {
            Text("DB 초기화 + 더미 삽입")
        }

        BottomNevBar()
    }
}

// 날짜 포맷 확장 함수
fun Date.formatToString(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(this)
}
