package com.example.allaccountbook.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.allaccountbook.database.model.TransactionType

@Entity(tableName = "Transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val transactionId : Int = 0,
    var transactionType : TransactionType,
    @ColumnInfo(name = "latitude")
    var latitude :  Double? = null,
    @ColumnInfo(name = "longitude")
    var longitude : Double? = null
)