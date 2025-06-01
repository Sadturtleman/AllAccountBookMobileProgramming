package com.example.allaccountbook.viewmodel.view


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allaccountbook.database.entity.ExpenseEntity
import com.example.allaccountbook.database.entity.IncomeEntity
import com.example.allaccountbook.database.entity.SavingEntity
import com.example.allaccountbook.database.model.TransactionCategory
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.getDate
import com.example.allaccountbook.database.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionDetail>>(emptyList())
    val transactions: StateFlow<List<TransactionDetail>> = _transactions

    init {
        loadAllTransactions()
        insertIfEmpty()
    }

    private fun loadAllTransactions() {
        viewModelScope.launch {
            _transactions.value = repository.getAllTransactionWithDetails()
        }
    }

    private fun insertMockData() {
        viewModelScope.launch {
            val mockData = listOf(
                TransactionDetail.Expense(
                    ExpenseEntity(
                        transactionId = 1,
                        price = 12000,
                        name = "스타벅스",
                        date = Calendar.getInstance().apply { set(2025, 4, 5) }.time,
                        category = TransactionCategory.FOOD
                    )
                ),
                TransactionDetail.Expense(
                    ExpenseEntity(
                        transactionId = 2,
                        price = 45000,
                        name = "홈플러스",
                        date = Calendar.getInstance().apply { set(2025, 4, 12) }.time,
                        category = TransactionCategory.SHOPPING
                    )
                ),
                TransactionDetail.Expense(
                    ExpenseEntity(
                        transactionId = 3,
                        price = 3000,
                        name = "버스 요금",
                        date = Calendar.getInstance().apply { set(2025, 4, 20) }.time,
                        category = TransactionCategory.TRANSPORT
                    )
                ),
                TransactionDetail.Income(
                    IncomeEntity(
                        transactionId = 4,
                        price = 2000000,
                        name = "급여",
                        date = Calendar.getInstance().apply { set(2025, 4, 25) }.time,
                        category = TransactionCategory.ETC
                    )
                ),
                TransactionDetail.Saving(
                    SavingEntity(
                        transactionId = 5,
                        price = 500000,
                        name = "예금",
                        startDate = Calendar.getInstance().apply { set(2025, 4, 1) }.time,
                        endDate = Calendar.getInstance().apply { set(2026, 4, 1) }.time,
                        category = TransactionCategory.ETC,
                        savingId = 1,
                        percent = 0.3F
                    )
                )
            )

            mockData.forEach { repository.insertDetail(it) }
            loadAllTransactions()
        }
    }

    private fun insertIfEmpty(){
        viewModelScope.launch {
            if (repository.getAllTransactionWithDetails().isEmpty()){
                insertMockData()
            }
        }
    }

    suspend fun getTransactionsByDate(dateString: String): List<TransactionDetail> {
        val parsed = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).parse(dateString)
        val formatted = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(parsed!!)
        return repository.getAllTransactionWithDetails().filter {
            it.getDate() == formatted
        }
    }
}
