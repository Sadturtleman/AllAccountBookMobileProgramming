package com.example.allaccountbook.database.model

import androidx.room.TypeConverter

class CategoryConverter {
    @TypeConverter
    fun fromCategory(category: TransactionCategory): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(name: String): TransactionCategory {
        return TransactionCategory.valueOf(name)
    }
}
