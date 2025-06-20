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
import com.example.allaccountbook.database.entity.InvestEntity
import com.example.allaccountbook.database.entity.SavingEntity
import com.example.allaccountbook.database.model.InvestType
import com.example.allaccountbook.model.BorrowMoney
import com.example.allaccountbook.model.BorrowType
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog
import com.example.allaccountbook.viewmodel.view.BorrowViewModel
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

enum class AddType(val display: String) {
    BORROWED("빌려준"),
    BORROW("빌린"),
    INCOME("수입"),
    EXPENSE("지출"),
    INVESTMENT("투자"),
    SAVING("저축")
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
    var isFixed by remember { mutableStateOf(false) }
    var fixedMonths by remember { mutableStateOf(1) } // 기본 1개월
    var investmentName by remember { mutableStateOf("") }
    var investmentCount by remember { mutableStateOf("") }
    var investmentPrice by remember { mutableStateOf("") }
    var investmentType by remember { mutableStateOf(InvestType.BUY) }
    var investmentCompany by remember { mutableStateOf("") }
    var investmentTendency by remember { mutableStateOf("") }
    var savingName by remember{mutableStateOf("")}
    var savingPrice by remember{mutableStateOf("")}
    var savingInterest by remember { mutableStateOf("") } // 연이율(%) 입력용
    var isGoal by remember { mutableStateOf(true) } // 기본값 true 또는 false는 자유

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("항목 추가", style = MaterialTheme.typography.titleLarge)

        if(selectedType != AddType.INVESTMENT && selectedType != AddType.SAVING){
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("사유") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if(selectedType != AddType.INVESTMENT && selectedType != AddType.SAVING) {
            OutlinedTextField(
                value = price,
                onValueChange = { price = it.filter { it.isDigit() } },
                label = { Text("금액") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        if(selectedType == AddType.INVESTMENT){
            OutlinedTextField(
                value = investmentName,
                onValueChange = { investmentName = it },
                label = { Text("종목명") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("매매 종류:")
                DropdownInvestmentTypeSelector(
                    investmentType = investmentType,
                    onSelect = { investmentType = it }
                )
            }
            OutlinedTextField(
                value = investmentCount,
                onValueChange = { investmentCount = it },
                label = { Text("매수 개수") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = investmentPrice,
                onValueChange = { investmentPrice = it },
                label = { Text("매매 단가") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = investmentCompany,
                onValueChange = { investmentCompany = it },
                label = { Text("증권사") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = investmentTendency,
                onValueChange = { investmentTendency = it },
                label = { Text("투자 성향") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (selectedType == AddType.INCOME || selectedType == AddType.EXPENSE) {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("카테고리(예: 음식점, 문화시설, 기타)") },
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

        if(selectedType == AddType.SAVING){
            OutlinedTextField(
                value = savingName,
                onValueChange = { savingName = it },
                label = { Text("저축 이름") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = savingPrice,
                onValueChange = { savingPrice = it },
                label = { Text("저축 금액") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = savingInterest,
                onValueChange = { savingInterest = it.filter { ch -> ch.isDigit() || ch == '.' } },
                label = { Text("연이율 (%)") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("목표 저축으로 설정:")
                Switch(checked = isGoal, onCheckedChange = { isGoal = it })
            }

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
                    val parsedDate =
                        SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA).parse(selectedDateString)
                    val formatted =
                        SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(parsedDate!!)
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
        if (selectedType == AddType.INCOME || selectedType == AddType.EXPENSE) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("고정비 여부:")
                Switch(checked = isFixed, onCheckedChange = { isFixed = it })
            }

            if (isFixed) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("지속 개월 수:")
                    DropdownMenuMonthSelector(
                        selectedMonth = fixedMonths,
                        onSelect = { fixedMonths = it }
                    )
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.End)){
            Button(
                onClick = { navController.popBackStack() },

                ) {
                Text("취소")
            }
            Spacer(modifier = Modifier.size(10.dp))
            Button(
                onClick = {
                    try {
                        val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(date)
                        val baseDate = parsedDate ?: Date()
                        val baseCalendar = Calendar.getInstance().apply {
                            time = baseDate
                        }
                        val originalDay = baseCalendar.get(Calendar.DAY_OF_MONTH)

                        val repeatCount =
                            if ((selectedType == AddType.INCOME || selectedType == AddType.EXPENSE) && isFixed) fixedMonths else 1

                        for (i in 0 until repeatCount) {
                            val calendar = Calendar.getInstance().apply {
                                time = baseDate
                                add(Calendar.MONTH, i)
                                set(Calendar.DAY_OF_MONTH, 1)
                                val maxDay = getActualMaximum(Calendar.DAY_OF_MONTH)
                                set(Calendar.DAY_OF_MONTH, minOf(originalDay, maxDay))
                            }

                            val currentDate = calendar.time

                            when (selectedType) {
                                AddType.BORROWED, AddType.BORROW -> {
                                    if (i == 0) { // 고정비 아님
                                        val newBorrow = BorrowMoney(
                                            id = 0,
                                            type = if (selectedType == AddType.BORROWED) BorrowType.BORROWED else BorrowType.BORROW,
                                            person = person,
                                            price = price.toIntOrNull() ?: 0,
                                            date = currentDate,
                                            reason = reason,
                                            finished = false
                                        )
                                        borrowViewModel.addBorrow(newBorrow)
                                    }
                                }

                                AddType.EXPENSE -> {
                                    transactionViewModel.viewModelScope.launch {
                                        transactionViewModel.addExpense(
                                            ExpenseEntity(
                                                expenseId = 0,
                                                transactionId = 0,
                                                price = price.toIntOrNull() ?: 0,
                                                name = reason,
                                                date = currentDate,
                                                category = if (category.isNotBlank()) category else "기타",
                                                isFixed = isFixed
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
                                                date = currentDate,
                                                category = if (category.isNotBlank()) category else "기타",
                                                isFixed = isFixed
                                            )
                                        )
                                    }
                                }

                                AddType.SAVING -> {
                                    transactionViewModel.viewModelScope.launch {
                                        transactionViewModel.addSaving(
                                            SavingEntity(
                                                transactionId = 0, // transactionId 추가
                                                price = savingPrice.toIntOrNull() ?: 0,
                                                name = savingName,
                                                startDate = currentDate,
                                                endDate = currentDate,
                                                isGoal = isGoal,
                                                percent = savingInterest.toFloatOrNull() ?: 0f,
                                            )
                                        )
                                    }
                                }

                                AddType.INVESTMENT -> {
                                    transactionViewModel.viewModelScope.launch {
                                        transactionViewModel.addInvestment(
                                            InvestEntity(
                                                transactionId = 0, // transactionID 추가
                                                count = investmentCount.toIntOrNull() ?: 0,
                                                price = investmentPrice.toIntOrNull() ?: 0,
                                                name = investmentName,
                                                date = currentDate,
                                                type = investmentType,
                                                company = investmentCompany,
                                                category = investmentTendency
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        navController.popBackStack()
                    } catch (e: Exception) {
                        // TODO: 오류 처리
                    }
                }
            ) {
                Text("등록")
            }
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
        AddType.EXPENSE to "지출",
        AddType.SAVING to "저축",
        AddType.INVESTMENT to "투자"
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

@Composable
fun DropdownMenuMonthSelector(
    selectedMonth: Int,
    onSelect: (Int) -> Unit
) {
    val options = (1..24).toList()
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text("${selectedMonth}개월")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { month ->
                DropdownMenuItem(
                    text = { Text("$month 개월") },
                    onClick = {
                        onSelect(month)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DropdownInvestmentTypeSelector(
    investmentType: InvestType,
    onSelect: (InvestType) -> Unit
) {
    val options = listOf(
        InvestType.BUY to "매수",
        InvestType.SELL to "매도"
    )

    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(options.first { it.first == investmentType }.second)
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