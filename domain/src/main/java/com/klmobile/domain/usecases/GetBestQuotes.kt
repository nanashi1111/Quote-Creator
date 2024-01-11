package com.klmobile.domain.usecases

import android.util.Log
import com.klmobile.data.dto.QuoteCategoryDTO
import com.klmobile.data.dto.QuoteDTO
import com.klmobile.data.repository.QuotesRepository
import com.klmobile.domain.State
import com.klmobile.domain.UseCase
import com.klmobile.domain.entities.BestQuotesView
import com.klmobile.domain.entities.QuoteCategoryView
import com.klmobile.domain.entities.QuoteContentView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBestQuotes @Inject constructor(private val quotesRepository: QuotesRepository) : UseCase<List<BestQuotesView>, Unit>() {
  override fun buildFlow(param: Unit): Flow<State<List<BestQuotesView>>> {
    return flow {
      val result = mutableListOf<BestQuotesView>()
      val categories = quotesRepository.getAllCategories()
      categories.forEach {
        val quoteCategoryView = QuoteCategoryView(it.categoryName)
        val quoteContentView = getBestQuoteFromCategory(it.categoryName)
        result.add(BestQuotesView(quoteCategoryView, quoteContentView))
      }
      emit(State.DataState(result))
    }
  }

  private fun getBestQuoteFromCategory(categoryName:String): QuoteContentView {
    val quote = quotesRepository.getQuotesOfCategory(categoryName).filter {
      it.content.length <= 40
    }.shuffled().firstOrNull() ?: QuoteDTO(category = categoryName, content = "")
    return QuoteContentView(categoryName, quote.content)
  }
}