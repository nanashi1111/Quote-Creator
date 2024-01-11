package com.klmobile.data.repository

import com.klmobile.data.QuotesSource
import com.klmobile.data.QuotesSource.listAloneQuote
import com.klmobile.data.QuotesSource.listFamilyQuote
import com.klmobile.data.QuotesSource.listFatherQuote
import com.klmobile.data.QuotesSource.listFriendQuote
import com.klmobile.data.QuotesSource.listFunnyQuote
import com.klmobile.data.QuotesSource.listHappinessQuote
import com.klmobile.data.QuotesSource.listLifeQuote
import com.klmobile.data.QuotesSource.listLoveQuote
import com.klmobile.data.QuotesSource.listMotherQuote
import com.klmobile.data.QuotesSource.listMotivationQuote
import com.klmobile.data.QuotesSource.listPositiveQuote
import com.klmobile.data.QuotesSource.listSadQuote
import com.klmobile.data.QuotesSource.listTrustQuote
import com.klmobile.data.QuotesSource.listWomenQuote
import com.klmobile.data.database.QuoteDao
import com.klmobile.data.dto.QuoteCategoryDTO
import com.klmobile.data.dto.QuoteDTO


interface QuotesRepository {
  fun isDataAvailable(): Boolean
  fun prepareData()

  fun getAllCategories(): List<QuoteCategoryDTO>
  fun getQuotesOfCategory(categoryName: String): List<QuoteDTO>
}

class QuotesRepositoryImpl(private val quoteDao: QuoteDao, private val quotesContentRepository: QuotesContentRepository): QuotesRepository {
  override fun isDataAvailable() = quoteDao.getCategories().isNotEmpty()
  override fun prepareData() {
    val categories = QuotesSource.categories.map { QuoteCategoryDTO(categoryName = it) }
    val quotes = mutableListOf<QuoteDTO>().apply {
      addAll(listLoveQuote.map { QuoteDTO(category = "Love", content = it )})
      addAll(listFriendQuote.map { QuoteDTO(category = "Friendship", content = it )})
      addAll(listMotivationQuote.map { QuoteDTO(category = "Motivation", content = it )})
      addAll(listFamilyQuote.map { QuoteDTO(category = "Family", content = it )})
      addAll(listHappinessQuote.map { QuoteDTO(category = "Happiness", content = it )})
      addAll(listWomenQuote.map { QuoteDTO(category = "Women", content = it )})
      addAll(listMotherQuote.map { QuoteDTO(category = "Mother", content = it )})
      addAll(listSadQuote.map { QuoteDTO(category = "Sad", content = it )})
      addAll(listFatherQuote.map { QuoteDTO(category = "Father", content = it )})
      addAll(listPositiveQuote.map { QuoteDTO(category = "Positive", content = it )})
      addAll(listAloneQuote.map { QuoteDTO(category = "Alone", content = it )})
      addAll(listTrustQuote.map { QuoteDTO(category = "Trust", content = it )})
      addAll(listFunnyQuote.map { QuoteDTO(category = "Funny", content = it )})
      addAll(listLifeQuote.map { QuoteDTO(category = "Life", content = it )})

    }
    quotesContentRepository.prepareQuoteCategories(categories)
    quotesContentRepository.prepareQuotes(quotes)
  }
  override fun getAllCategories(): List<QuoteCategoryDTO> = quoteDao.getCategories()
  override fun getQuotesOfCategory(categoryName: String) = quoteDao.getQuotesFromCategory(categoryName)
}