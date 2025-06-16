package com.example.allaccountbook.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.allaccountbook.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun InsertTransaction(entity : TransactionEntity) : Long

    @Delete
    suspend fun DeleteTransaction(entity: TransactionEntity)

    @Update
    suspend fun UpdateTransaction(entity: TransactionEntity)

    @Query("SELECT * FROM `Transaction`")
    fun getAllTransaction(): Flow<List<TransactionEntity>>
}