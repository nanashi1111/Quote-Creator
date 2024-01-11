package com.klmobile.domain.usecases

import android.util.Log
import com.klmobile.data.repository.QuotesRepository
import com.klmobile.domain.State
import com.klmobile.domain.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PrepareQuotesIfNeeded @Inject constructor(private val quotesRepository: QuotesRepository): UseCase<Unit, Unit>() {
  override fun buildFlow(param: Unit): Flow<State<Unit>> {
    return flow {
      val dataAvailable = quotesRepository.isDataAvailable()
      if (dataAvailable) {
        emit(State.DataState(Unit))
      } else {
        quotesRepository.prepareData()
        emit(State.DataState(Unit))
      }
    }
  }
}