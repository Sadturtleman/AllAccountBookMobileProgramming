package com.example.allaccountbook.uiComponent.add
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.allaccountbook.database.entity.ExpenseEntity
import com.example.allaccountbook.database.entity.IncomeEntity
import com.example.allaccountbook.model.BorrowMoney
import com.example.allaccountbook.model.BorrowType
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog
import com.example.allaccountbook.viewmodel.view.BorrowViewModel
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class AddType(val display: String) {
    BORROWED("빌려준"),
    BORROW("빌린"),
    INCOME("수입"),
    EXPENSE("지출")
}

@Composable
fun AddBorrowItemScreen(
    navController: NavController,
    borrowViewModel: BorrowViewModel = hiltViewModel(),
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    var reason by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var person by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") } // yyyy-MM-dd

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(AddType.BORROWED) }
    var category by remember { mutableStateOf("") } // 分类字段

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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


        if (selectedType == AddType.INCOME || selectedType == AddType.EXPENSE) {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("카테고리(예: 음식, 교통, 기타)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (selectedType == AddType.BORROWED || selectedType == AddType.BORROW) {
            OutlinedTextField(
                value = person,
                onValueChange = { person = it },
                label = { Text("대상") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("날짜 (yyyy-MM-dd)") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { showDatePickerDialog = true }) {
                Text("날짜 선택")
            }
        }

        if (showDatePickerDialog) {
            CustomDatePickerDialog(
                showDialog = showDatePickerDialog,
                onDismiss = { showDatePickerDialog = false },
                onDateSelected = { selectedDateString ->
                    val parsedDate = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA).parse(selectedDateString)
                    val formatted = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(parsedDate!!)
                    date = formatted
                    showDatePickerDialog = false
                }
            )
        }

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
                    when (selectedType) {
                        AddType.BORROWED, AddType.BORROW -> {
                            val newBorrow = BorrowMoney(
                                id = 0,
                                type = if (selectedType == AddType.BORROWED) BorrowType.BORROWED else BorrowType.BORROW,
                                person = person,
                                price = price.toIntOrNull() ?: 0,
                                date = parsedDate ?: Date(),
                                reason = reason,
                                finished = false
                            )
                            borrowViewModel.addBorrow(newBorrow)
                        }
                        AddType.EXPENSE -> {
                            transactionViewModel.viewModelScope.launch {
                                transactionViewModel.addExpense(
                                    ExpenseEntity(
                                        expenseId = 0,
                                        transactionId = 0,
                                        price = price.toIntOrNull() ?: 0,
                                        name = reason,
                                        date = parsedDate ?: Date(),
                                        category = if (category.isNotBlank()) category else "기타"
                                    )
                                )
                            }
                        }
                        AddType.INCOME -> {
                            transactionViewModel.viewModelScope.launch {
                                transactionViewModel.addIncome(
                                    IncomeEntity(
                                        incomeId = 0,
                                        transactionId = 0,
                                        price = price.toIntOrNull() ?: 0,
                                        name = reason,
                                        date = parsedDate ?: Date(),
                                        category = if (category.isNotBlank()) category else "기타"
                                    )
                                )
                            }
                        }
                    }
                    navController.popBackStack()
                } catch (e: Exception) {
                    // TODO: 오류 처리
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
    selectedType: AddType,
    onSelect: (AddType) -> Unit
) {
    val options = listOf(
        AddType.BORROWED to "빌려준",
        AddType.BORROW to "빌린",
        AddType.INCOME to "수입",
        AddType.EXPENSE to "지출"
    )
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
