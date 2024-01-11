package com.klmobile.domain.usecases

import android.util.Log
import com.klmobile.data.repository.QuotesRepository
import com.klmobile.domain.State
import com.klmobile.domain.UseCase
import com.klmobile.domain.entities.QuoteCategoryView
import com.klmobile.domain.entities.QuoteContentView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetQuotesFromCategory @Inject constructor(private val quotesRepository: QuotesRepository) : UseCase<List<QuoteContentView>, QuoteCategoryView>() {
  override fun buildFlow(param: QuoteCategoryView): Flow<State<List<QuoteContentView>>> {
    return flow {
      val result = quotesRepository.getQuotesOfCategory(param.categoryName).map {
        QuoteContentView(categoryName = param.categoryName, content = it.content)
      }
      emit(State.DataState(result))
    }
  }
}