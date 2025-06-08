package com.example.allaccountbook.database.model

import com.example.allaccountbook.database.entity.ExpenseEntity
import com.example.allaccountbook.database.entity.IncomeEntity
import com.example.allaccountbook.database.entity.SavingEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class TransactionDetail(val type: TransactionType) {
    data class Saving(val data: SavingEntity) : TransactionDetail(TransactionType.SAVING)
    data class Expense(val data: ExpenseEntity) : TransactionDetail(TransactionType.EXPENSE)
    data class Income(val data: IncomeEntity) : TransactionDetail(TransactionType.INCOME)
}

fun TransactionDetail.getDate(): String = when (this) {
    is TransactionDetail.Expense -> formatDate(this.data.date)
    is TransactionDetail.Income -> formatDate(this.data.date)
    is TransactionDetail.Saving -> formatDate(this.data.startDate)
}

private fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    return sdf.format(date)
}


fun TransactionDetail.getAmount(): Int = when (this) {
    is TransactionDetail.Expense -> this.data.price
    is TransactionDetail.Income -> this.data.price
    is TransactionDetail.Saving -> this.data.price
}

fun TransactionDetail.getName(): String = when (this) {
    is TransactionDetail.Expense -> this.data.name
    is TransactionDetail.Income -> this.data.name
    is TransactionDetail.Saving -> this.data.name
}

fun TransactionDetail.getCategory(): String = when (this) {
    is TransactionDetail.Expense -> this.data.category
    is TransactionDetail.Income -> this.data.category
    is TransactionDetail.Saving -> this.data.category
}