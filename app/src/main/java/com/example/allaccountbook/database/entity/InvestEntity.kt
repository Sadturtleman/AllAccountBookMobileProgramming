package com.example.allaccountbook.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.allaccountbook.database.model.DateConverter
import com.example.allaccountbook.database.model.InvestTypeConverter
import java.util.Date

@Entity(
    tableName = "Invest",
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

@TypeConverters(DateConverter::class, InvestTypeConverter::class)
data class InvestEntity (
    @PrimaryKey(autoGenerate = true)
    val investId : Int = 0,
    var transactionId : Int,
    var count : Int,
    var price : Int,
    var name : String,
    var date : Date
)