package com.klmobile.data.injection

import android.content.Context
import androidx.room.Room
import com.klmobile.data.database.QuoteDao
import com.klmobile.data.database.QuoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
  @Provides
  @Singleton
  fun provideQuoteDao(@ApplicationContext context: Context): QuoteDao {
    return Room.databaseBuilder(context, QuoteDatabase::class.java, "Quote_Database")
      .fallbackToDestructiveMigration().build().quoteDao()
  }
}