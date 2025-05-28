package com.example.allaccountbook.database.repository

import com.example.allaccountbook.database.dao.SavingDAO
import com.example.allaccountbook.database.entity.SavingEntity
import javax.inject.Inject

class SavingRepository @Inject constructor(
    private val dao: SavingDAO
) {
    suspend fun insert(saving: SavingEntity) {
        dao.InsertSaving(saving)
    }

    suspend fun update(saving: SavingEntity) {
        dao.UpdateSaving(saving)
    }

    suspend fun delete(saving: SavingEntity) {
        dao.DeleteSaving(saving)
    }
}