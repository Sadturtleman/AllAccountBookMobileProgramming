package com.example.allaccountbook.database.model

import com.example.allaccountbook.database.entity.ExpenseEntity
import com.example.allaccountbook.database.entity.IncomeEntity
import com.example.allaccountbook.database.entity.SavingEntity
import com.example.allaccountbook.database.entity.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class TransactionDetail(
    val type: TransactionType,
    open val latitude: Double?,
    open val longitude: Double?
) {
    data class Saving(
        val data: SavingEntity,
        override val latitude: Double?, override val longitude: Double?
    ) : TransactionDetail(TransactionType.SAVING, latitude, longitude)

    data class Expense(
        val data: ExpenseEntity,
        override val latitude: Double?, override val longitude: Double?
    ) : TransactionDetail(TransactionType.EXPENSE, latitude, longitude)

    data class Income(
        val data: IncomeEntity,
        override val latitude: Double?, override val longitude: Double?
    ) : TransactionDetail(TransactionType.INCOME, latitude, longitude)
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

fun TransactionEntity.getLocation(): Pair<Double?, Double?> =
    latitude to longitude
