package com.example.allaccountbook.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.allaccountbook.database.dao.*
import com.example.allaccountbook.database.entity.*

@Database(
    entities = [
        TransactionEntity::class,
        SavingEntity::class,
        IncomeEntity::class,
        ExpenseEntity::class,
        BorrowEntity::class
    ],
    version = 5, // 현재 개발 버전에 맞게 증가시킴
    exportSchema = false
)
abstract class AccountDB : RoomDatabase() {
    abstract fun getTransactionDao(): TransactionDAO
    abstract fun getSavingDao(): SavingDAO
    abstract fun getIncomeDao(): IncomeDAO
    abstract fun getExpenseDao(): ExpenseDAO
    abstract fun getBorrowDao(): BorrowDAO

    companion object {
        @Volatile
        private var DBInstance: AccountDB? = null

        fun getDBInstance(context: Context): AccountDB {
            return DBInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountDB::class.java,
                    "AccountDB"
                ).build()

                DBInstance = instance
                instance
            }
        }
    }
}
