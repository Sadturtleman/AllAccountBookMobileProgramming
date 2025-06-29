package com.example.allaccountbook.database.repository

import android.util.Log
import com.example.allaccountbook.database.dao.ExpenseDAO
import com.example.allaccountbook.database.dao.IncomeDAO
import com.example.allaccountbook.database.dao.InvestDAO
import com.example.allaccountbook.database.dao.SavingDAO
import com.example.allaccountbook.database.dao.TransactionDAO
import com.example.allaccountbook.database.entity.InvestEntity
import com.example.allaccountbook.database.entity.TransactionEntity
import com.example.allaccountbook.database.model.TransactionCategory
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.TransactionType
import com.example.allaccountbook.database.model.getLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDAO: TransactionDAO,
    private val savingDAO: SavingDAO,
    private val incomeDAO: IncomeDAO,
    private val expenseDAO: ExpenseDAO,
    private val investDAO: InvestDAO
) {
    suspend fun insert(transaction: TransactionEntity) {
        transactionDAO.InsertTransaction(transaction)
    }

    suspend fun update(transaction: TransactionEntity) {
        transactionDAO.UpdateTransaction(transaction)
    }

    suspend fun delete(transaction: TransactionEntity) {
        transactionDAO.DeleteTransaction(transaction)
    }

    fun getAllTransactionWithDetails(): Flow<List<TransactionDetail>> {
        return transactionDAO.getAllTransaction().map { transactions ->
            transactions.mapNotNull { transaction ->
                val (lat, lng) = transaction.getLocation()
                when (transaction.transactionType) {
                    TransactionType.SAVING -> savingDAO.getByTransactionId(transaction.transactionId)?.let {
                        TransactionDetail.Saving(it)
                    }
                    TransactionType.INCOME -> incomeDAO.getByTransactionId(transaction.transactionId)?.let {
                        TransactionDetail.Income(it, lat, lng)
                    }
                    TransactionType.EXPENSE -> expenseDAO.getByTransactionId(transaction.transactionId)?.let {
                        TransactionDetail.Expense(it, lat, lng)
                    }
                    TransactionType.INVEST -> investDAO.getByTransactionId(transaction.transactionId)?.let {
                        TransactionDetail.Invest(it)
                    }
                }
            }
        }
    }

    suspend fun insertDetail(detail: TransactionDetail) {
        when (detail) {
            is TransactionDetail.Saving -> {
                val transaction = TransactionEntity(transactionType = TransactionType.SAVING, latitude = detail.latitude, longitude = detail.longitude)
                val transactionId = transactionDAO.InsertTransaction(transaction)
                savingDAO.InsertSaving(detail.data.copy(transactionId = transactionId.toInt()))
            }

            is TransactionDetail.Income -> {
                val transaction = TransactionEntity(transactionType = TransactionType.INCOME, latitude = detail.latitude, longitude = detail.longitude)
                val transactionId = transactionDAO.InsertTransaction(transaction)
                incomeDAO.InsertIncome(detail.data.copy(transactionId = transactionId.toInt()))
            }

            is TransactionDetail.Expense -> {
                val transaction = TransactionEntity(transactionType = TransactionType.EXPENSE, latitude = detail.latitude, longitude = detail.longitude)
                val transactionId = transactionDAO.InsertTransaction(transaction)
                expenseDAO.InsertExpense(detail.data.copy(transactionId = transactionId.toInt()))
            }

            is TransactionDetail.Invest -> {
                val transaction = TransactionEntity(transactionType = TransactionType.INVEST, latitude = null, longitude = null)
                val transactionId = transactionDAO.InsertTransaction(transaction)
                investDAO.InsertInvest(detail.data.copy(transactionId = transactionId.toInt()))
            }
        }
    }

    suspend fun getAllUsedCategories(): List<String> {
        val incomeCats = incomeDAO.getAllIncomeCategories()
        val expenseCats = expenseDAO.getAllExpenseCategories()
        return (incomeCats + expenseCats).distinct()

    }
    suspend fun insertAndGetId(transaction: TransactionEntity): Long {
        return transactionDAO.InsertTransaction(transaction)
    }

    suspend fun insertExpenseIfNotExists(detail: TransactionDetail.Expense) {
        val existing = expenseDAO.findSimilarExpense(
            name = detail.data.name,
            price = detail.data.price,
            date = detail.data.date.time // long 비교
        )

        if (existing != null) {
            Log.d("TransactionRepo", "중복된 Expense 존재: ${existing.expenseId}, 삽입 생략")
            return
        }

        // 중복 없으면 삽입
        val transactionId = transactionDAO.InsertTransaction(
            TransactionEntity(
                transactionType = TransactionType.EXPENSE,
                latitude = detail.latitude,
                longitude = detail.longitude
            )
        ).toInt()

        expenseDAO.InsertExpense(detail.data.copy(transactionId = transactionId))
    }

    suspend fun getInvestByName(name : String) : List<InvestEntity>? {
        return investDAO.getByName(name)
    }

    suspend fun deleteDetail(detail: TransactionDetail) {
        when (detail) {
            is TransactionDetail.Saving -> {
                savingDAO.DeleteSaving(detail.data)
                transactionDAO.DeleteTransactionById(detail.data.transactionId)
            }

            is TransactionDetail.Income -> {
                incomeDAO.DeleteIncome(detail.data)
                transactionDAO.DeleteTransactionById(detail.data.transactionId)
            }

            is TransactionDetail.Expense -> {
                expenseDAO.DeleteExpense(detail.data)
                transactionDAO.DeleteTransactionById(detail.data.transactionId)
            }

            is TransactionDetail.Invest -> {
                investDAO.DeleteInvest(detail.data)
                transactionDAO.DeleteTransactionById(detail.data.transactionId)
            }
        }
    }

}
