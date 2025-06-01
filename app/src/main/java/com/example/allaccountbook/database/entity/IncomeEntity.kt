package com.example.allaccountbook.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.allaccountbook.database.model.CategoryConverter
import com.example.allaccountbook.database.model.DateConverter
import com.example.allaccountbook.database.model.TransactionCategory
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
@TypeConverters(DateConverter::class, CategoryConverter::class)
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true)
    val incomeId : Int = 0,
    var transactionId : Int,
    var price : Int,
    var name : String,
    var date : Date,
    var category: TransactionCategory
)