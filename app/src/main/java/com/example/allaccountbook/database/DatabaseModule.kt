package com.example.allaccountbook.database

import android.content.Context
import androidx.room.Room
import com.example.allaccountbook.database.dao.BorrowDAO
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
        ).build()
    }

    @Provides
    fun provideBorrowDao(accountDB: AccountDB): BorrowDAO {
        return accountDB.getBorrowDao()
    }

    // 필요하면 다른 DAO도 여기에 추가
}
