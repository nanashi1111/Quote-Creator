package com.klmobile.data.injection

import com.klmobile.data.database.QuoteDao
import com.klmobile.data.repository.QuotesContentRepository
import com.klmobile.data.repository.QuotesContentRepositoryImpl
import com.klmobile.data.repository.QuotesRepository
import com.klmobile.data.repository.QuotesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
  @Provides
  @Singleton
  fun provideQuoteContentRepository(quoteDao: QuoteDao): QuotesContentRepository = QuotesContentRepositoryImpl(quoteDao)
  @Provides
  @Singleton
  fun provideQuoteRepository(quoteDao: QuoteDao, quotesContentRepository: QuotesContentRepository): QuotesRepository = QuotesRepositoryImpl(quoteDao, quotesContentRepository)
}