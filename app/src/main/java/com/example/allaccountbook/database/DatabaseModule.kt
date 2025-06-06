package com.example.allaccountbook.database

import android.content.Context
import androidx.room.Room
import com.example.allaccountbook.database.dao.BorrowDAO
import com.example.allaccountbook.database.dao.ExpenseDAO
import com.example.allaccountbook.database.dao.IncomeDAO
import com.example.allaccountbook.database.dao.SavingDAO
import com.example.allaccountbook.database.dao.TransactionDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AccountDB {
        return Room.databaseBuilder(
            context,
            AccountDB::class.java,
            "AccountDB"
        )
            .fallbackToDestructiveMigration() // 버전 변경 시 기존 데이터 삭제 (lendBorrowScreen에서 충돌나서 제거)
            .build()
    }

    @Provides
    fun provideBorrowDao(accountDB: AccountDB): BorrowDAO {
        return accountDB.getBorrowDao()
    }

    @Provides
    fun provideTransactionDao(accountDB: AccountDB) : TransactionDAO{
        return accountDB.getTransactionDao()
    }

    @Provides
    fun provideSavingDao(accountDB: AccountDB) : SavingDAO{
        return accountDB.getSavingDao()
    }

    @Provides
    fun provideIncomeDao(accountDB: AccountDB) : IncomeDAO{
        return accountDB.getIncomeDao()
    }

    @Provides
    fun provideExpenseDao(accountDB: AccountDB) : ExpenseDAO{
        return accountDB.getExpenseDao()
    }
}
