package com.example.allaccountbook.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Income",
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["transactionId"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index(value = ["transactionId"], unique = true)]
)
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true)
    val incomeId : Int = 0,
    var transactionId : Int,
    var price : Int,
    var date : Date
)