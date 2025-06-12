package com.example.allaccountbook.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.allaccountbook.database.entity.InvestEntity

interface InvestDAO {
    @Insert
    suspend fun InsertInvest(entity : InvestEntity)

    @Delete
    suspend fun DeleteInvest(entity: InvestEntity)

    @Update
    suspend fun UpdateInvest(entity: InvestEntity)
}