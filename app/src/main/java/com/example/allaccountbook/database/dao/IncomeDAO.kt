package com.example.allaccountbook.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.allaccountbook.database.entity.IncomeEntity

@Dao
interface IncomeDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun InsertIncome(entity : IncomeEntity)

    @Delete
    suspend fun DeleteIncome(entity: IncomeEntity)

    @Update
    suspend fun UpdateIncome(entity: IncomeEntity)

    @Query("SELECT * FROM `Income` WHERE `Income`.transactionId = :id")
    suspend fun getByTransactionId(id : Int): IncomeEntity?

    @Query("SELECT DISTINCT category FROM Income")
    suspend fun getAllIncomeCategories(): List<String>

}