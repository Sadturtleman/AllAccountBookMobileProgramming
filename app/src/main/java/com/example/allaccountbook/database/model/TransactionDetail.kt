package com.example.allaccountbook.database.model

import com.example.allaccountbook.database.entity.ExpenseEntity
import com.example.allaccountbook.database.entity.IncomeEntity
import com.example.allaccountbook.database.entity.SavingEntity

sealed class TransactionDetail(val type: TransactionType) {
    data class Saving(val data: SavingEntity) : TransactionDetail(TransactionType.SAVING)
    data class Expense(val data: ExpenseEntity) : TransactionDetail(TransactionType.EXPENSE)
    data class Income(val data: IncomeEntity) : TransactionDetail(TransactionType.INCOME)
}