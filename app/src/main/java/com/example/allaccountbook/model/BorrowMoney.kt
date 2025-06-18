package com.example.allaccountbook.model

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

data class BorrowMoney(
    val type: BorrowType,
    val person: String,
    var price : Int,
    val id: Int,
    val date: Date,
    val reason : String,
    var finished : Boolean
)

fun BorrowMoney.getLocalDate(): LocalDate {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}
