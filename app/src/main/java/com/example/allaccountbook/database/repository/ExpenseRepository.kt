package com.example.allaccountbook.database.repository

import com.example.allaccountbook.database.dao.ExpenseDAO
import com.example.allaccountbook.database.entity.ExpenseEntity
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val dao: ExpenseDAO
) {
    suspend fun insert(expense: ExpenseEntity) {
        dao.InsertExpense(expense)
    }

    suspend fun update(expense: ExpenseEntity) {
        dao.UpdateExpense(expense)
    }

    suspend fun delete(expense: ExpenseEntity) {
        dao.DeleteExpense(expense)
    }
}