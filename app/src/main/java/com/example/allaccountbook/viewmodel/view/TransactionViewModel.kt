package com.example.allaccountbook.viewmodel.view

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.allaccountbook.database.entity.TransactionEntity
import com.example.allaccountbook.database.repository.TransactionRepository
import javax.inject.Inject

class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    val transaction = mutableStateListOf<TransactionEntity>()

}