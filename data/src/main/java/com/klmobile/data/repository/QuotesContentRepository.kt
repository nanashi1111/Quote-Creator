package com.klmobile.data.repository

import com.klmobile.data.database.QuoteDao
import com.klmobile.data.dto.QuoteCategoryDTO
import com.klmobile.data.dto.QuoteDTO

interface QuotesContentRepository {
  fun prepareQuotes(quotes: List<QuoteDTO>)
  fun prepareQuoteCategories(categories: List<QuoteCategoryDTO>)
}

class QuotesContentRepositoryImpl(private val quoteDao: QuoteDao): QuotesContentRepository {
  override fun prepareQuotes(quotes: List<QuoteDTO>) = quoteDao.addQuotes(quotes)

  override fun prepareQuoteCategories(categories: List<QuoteCategoryDTO>) = quoteDao.addCategories(categories)

}