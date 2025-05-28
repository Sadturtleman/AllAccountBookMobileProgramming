package com.example.allaccountbook.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.allaccountbook.database.entity.TransactionEntity

@Dao
interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun InsertTransaction(entity : TransactionEntity)

    @Delete
    suspend fun DeleteTransaction(entity: TransactionEntity)

    @Update
    suspend fun UpdateTransaction(entity: TransactionEntity)
}