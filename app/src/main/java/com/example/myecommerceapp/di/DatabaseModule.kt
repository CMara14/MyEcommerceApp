package com.example.myecommerceapp.di

import android.content.Context
import androidx.room.Room
import com.example.myecommerceapp.data.local.AppDatabase
import com.example.myecommerceapp.data.local.dao.CartItemDao
import com.example.myecommerceapp.data.local.dao.OrderHistoryDao
import com.example.myecommerceapp.data.local.dao.OrderItemDao
import com.example.myecommerceapp.data.local.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ecommerce_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    @Provides
    fun provideCartItemDao(db: AppDatabase): CartItemDao = db.cartItemDao()

    @Provides
    fun provideOrderHistoryDao(db: AppDatabase): OrderHistoryDao = db.orderHistoryDao()

    @Provides
    fun provideOrderItemDao(db: AppDatabase): OrderItemDao = db.orderItemDao()
}