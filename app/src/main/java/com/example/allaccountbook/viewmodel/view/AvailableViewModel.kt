package com.example.allaccountbook.viewmodel.view

import android.app.Application
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.allaccountbook.model.CategoryPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableViewModel @Inject constructor(
    private val context: Application
) : AndroidViewModel(context) {

    private val _categoryAmountMap = mutableStateMapOf<String, Int>()
    val categoryAmountMapState: SnapshotStateMap<String, Int> get() = _categoryAmountMap

    fun loadAmount(category: String) {
        viewModelScope.launch {
            CategoryPreference.getCategoryAmountFlow(context, category).collect {
                _categoryAmountMap[category] = it
            }
        }
    }

    fun saveAmount(category: String, amount: Int) {
        viewModelScope.launch {
            CategoryPreference.saveCategoryAmount(context, category, amount)
            _categoryAmountMap[category] = amount
        }
    }

    fun deleteCategoryAmount(category: String) {
        viewModelScope.launch {
            CategoryPreference.saveCategoryAmount(context, category, 0)
            _categoryAmountMap.remove(category)
        }
    }
    fun loadAllAmounts(categories: List<String>) {
        viewModelScope.launch {
            val all = CategoryPreference.getAllCategoryAmounts(context, categories)
            _categoryAmountMap.putAll(all)
        }
    }
    fun preloadAllCategoryAmounts(categories: List<String>) {
        viewModelScope.launch {
            val loaded = CategoryPreference.getAllCategoryAmounts(context, categories)
            _categoryAmountMap.putAll(loaded)
        }
    }

}