package com.example.allaccountbook.database.model

import androidx.room.TypeConverter

class InvestTypeConverter {
    @TypeConverter
    fun fromType(type : InvestType): String{
        return type.name
    }

    @TypeConverter
    fun toType(name : String) : InvestType{
        return InvestType.valueOf(name)
    }
}