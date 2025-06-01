package com.example.allaccountbook.uiComponent

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.allaccountbook.model.BorrowMoney
import com.example.allaccountbook.model.BorrowType
import com.example.allaccountbook.viewmodel.view.BorrowViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddBorrowItemScreen(
    navController: NavController,
    viewModel: BorrowViewModel = hiltViewModel()
) {
    var reason by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var person by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") } // yyyy-MM-dd
    var selectedType by remember { mutableStateOf(BorrowType.BORROWED) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("항목 추가", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            label = { Text("사유") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it.filter { it.isDigit() } },
            label = { Text("금액") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = person,
            onValueChange = { person = it },
            label = { Text("대상") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("날짜 (yyyy-MM-dd)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("종류:")
            DropdownMenuTypeSelector(
                selectedType = selectedType,
                onSelect = { selectedType = it }
            )
        }

        Button(
            onClick = {
                try {
                    val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(date)
                    val newBorrow = BorrowMoney(
                        id = 0,
                        type = selectedType,
                        person = person,
                        price = price.toIntOrNull() ?: 0,
                        date = parsedDate ?: Date(),
                        reason = reason,
                        finished = false
                    )
                    viewModel.addBorrow(newBorrow)
                    navController.popBackStack()
                } catch (e: Exception) {
                    // 로그 처리 또는 사용자에게 알림 필요 시 여기에 작성
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("등록")
        }
    }
}

@Composable
fun DropdownMenuTypeSelector(
    selectedType: BorrowType,
    onSelect: (BorrowType) -> Unit
) {
    val options = listOf(BorrowType.BORROWED to "빌려준", BorrowType.BORROW to "빌린")
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(options.first { it.first == selectedType }.second)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (type, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onSelect(type)
                        expanded = false
                    }
                )
            }
        }
    }
}
