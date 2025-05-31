package com.example.allaccountbook.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.allaccountbook.database.dao.BorrowDAO
import com.example.allaccountbook.database.dao.ExpenseDAO
import com.example.allaccountbook.database.dao.IncomeDAO
import com.example.allaccountbook.database.dao.SavingDAO
import com.example.allaccountbook.database.dao.TransactionDAO
import com.example.allaccountbook.database.entity.BorrowEntity
import com.example.allaccountbook.database.entity.ExpenseEntity
import com.example.allaccountbook.database.entity.IncomeEntity
import com.example.allaccountbook.database.entity.SavingEntity
import com.example.allaccountbook.database.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        SavingEntity::class,
        IncomeEntity::class,
        ExpenseEntity::class,
        BorrowEntity::class
    ],
    version = 1,
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
        private var DBInstanse: AccountDB? = null

        fun getDBInstance(context: android.content.Context): AccountDB {
            return DBInstanse ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountDB::class.java,
                    "AccountDB"
                ).build()
                DBInstanse = instance
                instance
            }
        }
    }
}