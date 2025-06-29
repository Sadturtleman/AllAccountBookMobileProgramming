package com.example.allaccountbook.model

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object CategoryPreference {
    private val Context.dataStore by preferencesDataStore(name = "category_prefs")
    private val CATEGORY_LIST_KEY = stringPreferencesKey("category_list")
    private val CATEGORY_AMOUNT_PREFIX = "category_amount_"

    fun getAmountKey(category: String): Preferences.Key<Int> =
        intPreferencesKey("$CATEGORY_AMOUNT_PREFIX$category")


    suspend fun saveCategoryList(context: Context, categories: List<String>) {
        context.dataStore.edit { prefs ->
            prefs[CATEGORY_LIST_KEY] = categories.joinToString(",")
        }
    }

    suspend fun saveCategoryAmount(context: Context, category: String, amount: Int) {
        context.dataStore.edit { prefs ->
            prefs[getAmountKey(category)] = amount
        }
    }

    fun getCategoryAmountFlow(context: Context, category: String): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[getAmountKey(category)] ?: getDefault(category)
        }
    }

    private fun getDefault(category: String): Int {
        return when (category) {
            "음식점" -> 100000
            "문화시설" -> 80000
            "기타" -> 60000
            else -> 50000
        }
    }

    suspend fun clearCategoryList(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(CATEGORY_LIST_KEY)
        }
    }
    suspend fun getAllCategoryAmounts(context: Context, categories: List<String>): Map<String, Int> {
        val prefs = context.dataStore.data.first()  // Flow를 collect하지 않고 직접 fetch
        return categories.associateWith { category ->
            prefs[getAmountKey(category)] ?: getDefault(category)
        }
    }

    fun getCategoryListFlow(context: Context): Flow<List<String>> =
        context.dataStore.data.map { prefs ->
            val raw = prefs[CATEGORY_LIST_KEY] ?: ""
            raw.split(",").filter { it.isNotBlank() }.ifEmpty { defaultCategories() }
        }


    private fun defaultCategories() = listOf("음식점", "문화시설")
}