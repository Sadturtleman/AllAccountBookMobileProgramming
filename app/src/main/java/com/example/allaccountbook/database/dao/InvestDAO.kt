package com.example.allaccountbook.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.allaccountbook.database.entity.IncomeEntity
import com.example.allaccountbook.database.entity.InvestEntity

@Dao
interface InvestDAO {
    @Insert
    suspend fun InsertInvest(entity : InvestEntity)

    @Delete
    suspend fun DeleteInvest(entity: InvestEntity)

    @Update
    suspend fun UpdateInvest(entity: InvestEntity)

    @Query("SELECT * FROM `Invest` WHERE `Invest`.transactionId = :id")
    suspend fun getByTransactionId(id : Int): InvestEntity?

    @Query("SELECT * FROM `Invest` WHERE `Invest`.name = :name")
    suspend fun getByName(name : String) : List<InvestEntity>?
}