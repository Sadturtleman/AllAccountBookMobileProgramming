package com.example.allaccountbook.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.allaccountbook.database.entity.BorrowEntity
import com.example.allaccountbook.model.BorrowMoney

@Dao
interface BorrowDAO {
    @Insert
    suspend fun insertBorrow(entity : BorrowEntity)

    @Delete
    suspend fun deleteBorrow(entity : BorrowEntity)

    @Update
    suspend fun updateBorrow(entity : BorrowEntity)

    @Query("SELECT * FROM `Borrow`")
    suspend fun getAllBorrow() : List<BorrowEntity>

    @Query("DELETE FROM `Borrow`")
    suspend fun deleteAll()

}