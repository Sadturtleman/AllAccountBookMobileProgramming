package com.example.allaccountbook.database.repository

import com.example.allaccountbook.database.dao.TransactionDAO
import com.example.allaccountbook.database.entity.TransactionEntity
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val dao: TransactionDAO
) {
    suspend fun insert(transaction: TransactionEntity) {
        dao.InsertTransaction(transaction)
    }

    suspend fun update(transaction: TransactionEntity) {
        dao.UpdateTransaction(transaction)
    }

    suspend fun delete(transaction: TransactionEntity) {
        dao.DeleteTransaction(transaction)
    }
}