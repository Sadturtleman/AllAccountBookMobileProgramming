package com.example.allaccountbook.database.extension

import com.example.allaccountbook.database.entity.BorrowEntity
import com.example.allaccountbook.model.BorrowMoney

fun BorrowEntity.toBorrowMoney(): BorrowMoney {
    return BorrowMoney(
        id = borrowId,
        type = type,
        person = person,
        date = date,
        reason = reason,
        finished = finished
    )
}

fun BorrowMoney.toEntity(): BorrowEntity {
    return BorrowEntity(
        borrowId = this.id,
        type = this.type,
        person = this.person,
        date = this.date,
        reason = this.reason,
        finished = this.finished
    )
}
