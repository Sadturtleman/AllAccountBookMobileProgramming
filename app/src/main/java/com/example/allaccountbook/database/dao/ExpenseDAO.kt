package com.example.allaccountbook.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.allaccountbook.database.entity.ExpenseEntity

@Dao
interface ExpenseDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun InsertExpense(entity : ExpenseEntity)

    @Delete
    suspend fun DeleteExpense(entity: ExpenseEntity)

    @Update
    suspend fun UpdateExpense(entity: ExpenseEntity)

    @Query("SELECT * FROM `Expense` WHERE `Expense`.transactionId = :id")
    suspend fun getByTransactionId(id : Int): ExpenseEntity?

    @Query("SELECT DISTINCT category FROM Expense")
    suspend fun getAllExpenseCategories(): List<String>

    @Query("SELECT * FROM Expense WHERE name = :name AND price = :price AND date = :date LIMIT 1")
    suspend fun findSimilarExpense(name: String, price: Int, date: Long): ExpenseEntity?

}