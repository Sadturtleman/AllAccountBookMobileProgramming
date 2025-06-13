package com.example.allaccountbook.database.repository

import com.example.allaccountbook.database.dao.InvestDAO
import com.example.allaccountbook.database.entity.InvestEntity
import javax.inject.Inject

class InvestRepository @Inject constructor(
    private val dao: InvestDAO
){
    suspend fun insert(invest : InvestEntity){
        dao.InsertInvest(invest)
    }

    suspend fun update(invest : InvestEntity){
        dao.UpdateInvest(invest)
    }

    suspend fun delete(invest : InvestEntity){
        dao.DeleteInvest(invest)
    }
}