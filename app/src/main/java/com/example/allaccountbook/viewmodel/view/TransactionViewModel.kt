package com.example.allaccountbook.viewmodel.view


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allaccountbook.database.entity.ExpenseEntity
import com.example.allaccountbook.database.entity.IncomeEntity
import com.example.allaccountbook.database.entity.InvestEntity
import com.example.allaccountbook.database.entity.SavingEntity
import com.example.allaccountbook.database.model.InvestType
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.getLocalDate
import com.example.allaccountbook.database.model.getName
import com.example.allaccountbook.database.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    val transactions: StateFlow<List<TransactionDetail>> = repository.getAllTransactionWithDetails().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList())


    suspend fun getAllCategories():List<String>{
        return repository.getAllUsedCategories()
    }

    suspend fun addExpense(expense: ExpenseEntity) {
        repository.insertDetail(TransactionDetail.Expense(expense, null, null))
    }

    suspend fun addIncome(income: IncomeEntity) {
        repository.insertDetail(TransactionDetail.Income(income, null, null))
    }


    private fun insertMockData() {
        viewModelScope.launch {
            val mockData = listOf(
                TransactionDetail.Expense(
                    data = ExpenseEntity(
                        transactionId = 1,
                        price = 29000,
                        name = "스타벅스",
                        date = Calendar.getInstance().apply { set(2025, 4, 5) }.time,
                        category = "음식점"
                    ),
                    latitude = null,
                    longitude = null
                ),
                TransactionDetail.Expense(
                    data = ExpenseEntity(
                        transactionId = 2,
                        price = 45000,
                        name = "홈플러스",
                        date = Calendar.getInstance().apply { set(2025, 4, 12) }.time,
                        category = "문화시설"
                    ),
                    latitude = null,
                    longitude = null
                ),
                TransactionDetail.Expense(
                    data = ExpenseEntity(
                        transactionId = 3,
                        price = 3000,
                        name = "버스 요금",
                        date = Calendar.getInstance().apply { set(2025, 4, 20) }.time,
                        category = "교통 수단"
                    ),
                    latitude = null,
                    longitude = null
                ),
                TransactionDetail.Income(
                    data = IncomeEntity(
                        transactionId = 4,
                        price = 2000000,
                        name = "급여",
                        date = Calendar.getInstance().apply { set(2025, 4, 25) }.time,
                        category = "기타"
                    ),
                    latitude = null,
                    longitude = null
                ),
                TransactionDetail.Saving(
                    data = SavingEntity(
                        transactionId = 5,
                        price = 500000,
                        name = "예금",
                        startDate = Calendar.getInstance().apply { set(2025, 4, 1) }.time,
                        endDate = Calendar.getInstance().apply { set(2026, 4, 1) }.time,
                        category = "기타",
                        savingId = 1,
                        percent = 0.3F
                    ),
                    latitude = null,
                    longitude = null
                ),
                TransactionDetail.Invest(
                    data = InvestEntity(
                        transactionId = 6,
                        count = 2,
                        price = 1000,
                        name = "농심",
                        date = Calendar.getInstance().apply { set(2025, 4, 1) }.time,
                        category = "음식",
                        type = InvestType.BUY
                    )
                )
            )

            mockData.forEach { repository.insertDetail(it) }
        }
    }
}
