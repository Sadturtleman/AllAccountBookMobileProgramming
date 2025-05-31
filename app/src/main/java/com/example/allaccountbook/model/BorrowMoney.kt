package com.example.allaccountbook.model

import java.util.Date

data class BorrowMoney(
    val type: BorrowType,
    val person: String,
    val id: Int,
    val date: Date,
    val reason : String,
    var finished : Boolean
)