package com.example.allaccountbook.viewmodel.view

import android.app.Application
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.allaccountbook.model.CategoryPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AvailableViewModel @Inject constructor(
    private val context: Application
) : AndroidViewModel(context) {

    private val _categoryAmountMapState = MutableStateFlow<Map<String, Int>>(emptyMap())
    val categoryAmountMapState: StateFlow<Map<String, Int>> = _categoryAmountMapState

    private val _categoriesState = MutableStateFlow<List<String>>(emptyList())
    val categoriesState: StateFlow<List<String>> = _categoriesState

    init {
        viewModelScope.launch {

            CategoryPreference.getCategoryListFlow(context).collect { categories ->
                _categoriesState.value = categories
                loadAmounts(categories + "기타")
            }
        }
    }

    fun loadAmounts(categories: List<String>) {
        viewModelScope.launch {
            categories.forEach { category ->
                launch {
                    CategoryPreference.getCategoryAmountFlow(context, category).collectLatest { amount ->
                        _categoryAmountMapState.update { it + (category to amount) }
                    }
                }
            }
        }
    }

    fun saveAmount(category: String, amount: Int) {
        viewModelScope.launch {
            CategoryPreference.saveCategoryAmount(context, category, amount)
        }
    }

    fun deleteCategoryAmount(category: String) {
        viewModelScope.launch {
            CategoryPreference.saveCategoryAmount(context, category, 0)
            _categoryAmountMapState.update { it - category }
        }
    }

    fun addCategory(category: String) {
        val updated = _categoriesState.value + category
        _categoriesState.value = updated
        saveCategories(updated) // ✅ 반드시 저장
    }

    fun removeCategory(category: String) {
        val updated = _categoriesState.value - category
        _categoriesState.value = updated
        saveCategories(updated) // ✅ 반드시 저장
        deleteCategoryAmount(category)
    }


    private fun saveCategories(categories: List<String>) {
        viewModelScope.launch {
            CategoryPreference.saveCategoryList(context, categories)
        }
    }
}
