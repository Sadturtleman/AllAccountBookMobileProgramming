package com.example.allaccountbook.database.repository

import com.example.allaccountbook.database.dao.ExpenseDAO
import com.example.allaccountbook.database.dao.IncomeDAO
import com.example.allaccountbook.database.dao.SavingDAO
import com.example.allaccountbook.database.dao.TransactionDAO
import com.example.allaccountbook.database.entity.TransactionEntity
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.TransactionType
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
            when(transaction.transactionType){
                TransactionType.SAVING -> savingDAO.getByTransactionId(transaction.transactionId)?.let{
                    TransactionDetail.Saving(it)
                }

                TransactionType.INCOME -> incomeDAO.getByTransactionId(transaction.transactionId)?.let {
                    TransactionDetail.Income(it)
                }
                TransactionType.EXPENSE -> expenseDAO.getByTransactionId(transaction.transactionId)?.let {
                    TransactionDetail.Expense(it)
                }
            }
        }
    }
}