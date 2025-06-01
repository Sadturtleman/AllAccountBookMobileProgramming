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
        insertIfEmpty()
    }

    private fun insertIfEmpty(){
        viewModelScope.launch {
            if (repository.getAllBorrow().isEmpty()){
                insertDummyData()
            }
        }
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
        }
    }

    fun insertDummyData() {
        viewModelScope.launch {
            val dummyList = listOf(
                BorrowEntity(
                    type = BorrowType.BORROWED, // 내가 빌려준
                    price = 10000,
                    person = "철수",
                    date = Date(),
                    reason = "책값",
                    finished = false
                ),
                BorrowEntity(
                    type = BorrowType.BORROW, // 내가 빌린
                    price = 18000,
                    person = "영희",
                    date = Date(),
                    reason = "밥값",
                    finished = true
                ),
                BorrowEntity(
                    type = BorrowType.BORROWED,
                    price = 23000,
                    person = "민수",
                    date = Date(),
                    reason = "택시비",
                    finished = false
                ),
                BorrowEntity(
                    type = BorrowType.BORROW,
                    price = 30000,
                    person = "지민",
                    date = Date(),
                    reason = "커피값",
                    finished = false
                ),
                BorrowEntity(
                    type = BorrowType.BORROWED,
                    price = 40000,
                    person = "현수",
                    date = Date(),
                    reason = "회비",
                    finished = true
                )
            )

            dummyList.forEach { repository.insert(it) }

            loadAllBorrow()
        }
    }

    fun resetAndInsertDummyData() {
        viewModelScope.launch {
            repository.deleteAll()
            insertDummyData()
        }
    }

}