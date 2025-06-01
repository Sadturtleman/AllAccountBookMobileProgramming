package com.example.allaccountbook.database.repository

import com.example.allaccountbook.database.dao.BorrowDAO
import com.example.allaccountbook.database.entity.BorrowEntity
import com.example.allaccountbook.database.extension.toBorrowMoney
import com.example.allaccountbook.model.BorrowMoney
import com.example.allaccountbook.model.BorrowType
import javax.inject.Inject

class BorrowRepository @Inject constructor(
    private val dao: BorrowDAO
) {
    suspend fun insert(borrow: BorrowEntity) {
        dao.insertBorrow(borrow)
    }

    suspend fun update(borrow: BorrowEntity) {
        dao.updateBorrow(borrow)
    }

    suspend fun delete(borrow: BorrowEntity) {
        dao.deleteBorrow(borrow)
    }

    suspend fun getAllBorrow(): List<BorrowMoney> {
        val borrows = dao.getAllBorrow()

        return borrows.map { borrow ->
            borrow.toBorrowMoney()
        }
    }
    suspend fun deleteAll() {
        dao.deleteAll()
    }

}