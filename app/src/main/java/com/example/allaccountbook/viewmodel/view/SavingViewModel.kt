package com.example.allaccountbook.viewmodel.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.allaccountbook.database.entity.SavingEntity

class SavingViewModel : ViewModel() {
    var selectedSaving by mutableStateOf<SavingEntity?>(null)
}