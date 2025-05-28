package com.example.allaccountbook.database.repository

import com.example.allaccountbook.database.dao.IncomeDAO
import com.example.allaccountbook.database.entity.IncomeEntity
import javax.inject.Inject

class IncomeRepository @Inject constructor(
    private val dao: IncomeDAO
) {
    suspend fun insert(income: IncomeEntity) {
        dao.InsertIncome(income)
    }

    suspend fun update(income: IncomeEntity) {
        dao.UpdateIncome(income)
    }

    suspend fun delete(income: IncomeEntity) {
        dao.DeleteIncome(income)
    }
}