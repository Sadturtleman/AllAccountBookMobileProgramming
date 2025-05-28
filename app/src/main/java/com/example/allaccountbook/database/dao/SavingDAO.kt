package com.example.allaccountbook.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.allaccountbook.database.entity.SavingEntity

@Dao
interface SavingDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun InsertSaving(entity : SavingEntity)

    @Delete
    suspend fun DeleteSaving(entity: SavingEntity)

    @Update
    suspend fun UpdateSaving(entity: SavingEntity)

    @Query("SELECT * FROM `Saving` WHERE `Saving`.transactionId = :id")
    suspend fun getByTransactionId(id : Int) : SavingEntity?
}