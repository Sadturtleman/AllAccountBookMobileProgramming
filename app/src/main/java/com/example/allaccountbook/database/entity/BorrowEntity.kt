package com.example.allaccountbook.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.allaccountbook.database.model.DateConverter
import com.example.allaccountbook.model.BorrowType
import java.util.Date


@Entity(
    tableName = "Borrow"
)
@TypeConverters(DateConverter::class)
data class BorrowEntity(
    @PrimaryKey(autoGenerate = true)
    val borrowId : Int = 0,
    var type: BorrowType,
    val price : Int,
    val person : String,
    val date : Date,
    val reason : String,
    var finished : Boolean
)