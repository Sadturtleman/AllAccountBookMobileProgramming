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
    tableName = "Saving",
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
data class SavingEntity (
    @PrimaryKey(autoGenerate = true)
    val savingId : Int = 0,
    var transactionId : Int,
    var startDate : Date,
    var endDate : Date,
    var price : Int,
    var name : String,
    var percent : Float,
    var category: String = "저축",
    var isGoal : Boolean = true
)