package com.example.allaccountbook.model

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CategoryPreference {
    private val Context.dataStore by preferencesDataStore(name = "category_prefs")

    private val CATEGORY_AMOUNT_PREFIX = "category_amount_"

    fun getAmountKey(category: String): Preferences.Key<Int> =
        intPreferencesKey("$CATEGORY_AMOUNT_PREFIX$category")

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
}
