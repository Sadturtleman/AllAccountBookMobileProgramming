package com.example.allaccountbook.database.repository

import com.example.allaccountbook.database.dao.ExpenseDAO
import com.example.allaccountbook.database.dao.IncomeDAO
import com.example.allaccountbook.database.dao.SavingDAO
import com.example.allaccountbook.database.dao.TransactionDAO
import com.example.allaccountbook.database.entity.TransactionEntity
import com.example.allaccountbook.database.model.TransactionCategory
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.TransactionType
import com.example.allaccountbook.database.model.getLocation
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDAO: TransactionDAO,
    private val savingDAO: SavingDAO,
    private val incomeDAO: IncomeDAO,
    private val expenseDAO: ExpenseDAO
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

    suspend fun getAllTransactionWithDetails(): List<TransactionDetail>{
        val transactions = transactionDAO.getAllTransaction()

        return transactions.mapNotNull {
            transaction ->
            val (lat, lng) = transaction.getLocation()
            when(transaction.transactionType){
                TransactionType.SAVING -> savingDAO.getByTransactionId(transaction.transactionId)?.let{
                    TransactionDetail.Saving(it, lat, lng)
                }

                TransactionType.INCOME -> incomeDAO.getByTransactionId(transaction.transactionId)?.let {
                    TransactionDetail.Income(it, lat, lng)
                }
                TransactionType.EXPENSE -> expenseDAO.getByTransactionId(transaction.transactionId)?.let {
                    TransactionDetail.Expense(it, lat, lng)
                }
            }
        }
    }

    suspend fun insertDetail(detail: TransactionDetail) {
        when (detail) {
            is TransactionDetail.Saving -> {
                val transaction = TransactionEntity(transactionType = TransactionType.SAVING)
                val transactionId = transactionDAO.InsertTransaction(transaction)
                savingDAO.InsertSaving(detail.data.copy(transactionId = transactionId.toInt()))
            }

            is TransactionDetail.Income -> {
                val transaction = TransactionEntity(transactionType = TransactionType.INCOME)
                val transactionId = transactionDAO.InsertTransaction(transaction)
                incomeDAO.InsertIncome(detail.data.copy(transactionId = transactionId.toInt()))
            }

            is TransactionDetail.Expense -> {
                val transaction = TransactionEntity(transactionType = TransactionType.EXPENSE)
                val transactionId = transactionDAO.InsertTransaction(transaction)
                expenseDAO.InsertExpense(detail.data.copy(transactionId = transactionId.toInt()))
            }
        }
    }

    suspend fun getAllUsedCategories(): List<String> {
        val incomeCats = incomeDAO.getAllIncomeCategories()
        val expenseCats = expenseDAO.getAllExpenseCategories()
        return (incomeCats + expenseCats).distinct()

//        return all.mapNotNull { catName ->
//            try {
//                valueOf(catName)
//            } catch (e: IllegalArgumentException) {
//                null // enum에 없는 값은 무시
//            }
//        }.toSet()
    }
    suspend fun insertAndGetId(transaction: TransactionEntity): Long {
        return transactionDAO.InsertTransaction(transaction)
    }

}
