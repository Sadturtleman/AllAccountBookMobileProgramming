package com.example.allaccountbook.viewmodel.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allaccountbook.database.entity.BorrowEntity
import com.example.allaccountbook.database.extension.toEntity
import com.example.allaccountbook.database.repository.BorrowRepository
import com.example.allaccountbook.model.BorrowMoney
import com.example.allaccountbook.model.BorrowType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BorrowViewModel @Inject constructor(
    private val repository: BorrowRepository
) : ViewModel() {
    private val _borrowList = MutableStateFlow<List<BorrowMoney>>(emptyList())
    val borrowList: StateFlow<List<BorrowMoney>> get() = _borrowList

    init {
        loadAllBorrow()
    }

    fun loadAllBorrow(){
        viewModelScope.launch {
            _borrowList.value = repository.getAllBorrow()
        }
    }

    fun updateBorrow(borrow : BorrowMoney){
        viewModelScope.launch {
            repository.update(borrow.toEntity())
            loadAllBorrow()
        }
    }

    fun deleteBorrow(borrow: BorrowMoney){
        viewModelScope.launch {
            repository.delete(borrow.toEntity())
            loadAllBorrow()
        }
    }

    fun addBorrow(borrow: BorrowMoney){
        viewModelScope.launch {
            repository.insert(borrow.toEntity())
            loadAllBorrow()
        }
    }

    fun resetAndInsertDummyData() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

}
