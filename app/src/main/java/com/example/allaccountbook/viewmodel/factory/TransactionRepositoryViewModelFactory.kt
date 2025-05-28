package com.example.allaccountbook.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allaccountbook.database.repository.TransactionRepository
import com.example.allaccountbook.viewmodel.view.TransactionViewModel

class TransactionRepositoryViewModelFactory(private val repository: TransactionRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown View Model")
    }
}