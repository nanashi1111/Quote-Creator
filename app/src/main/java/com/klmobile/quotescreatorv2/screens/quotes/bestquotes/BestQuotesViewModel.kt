package com.klmobile.quotescreatorv2.screens.quotes.bestquotes

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klmobile.domain.State
import com.klmobile.domain.entities.BestQuotesView
import com.klmobile.domain.usecases.GetBestQuotes
import com.klmobile.quotescreatorv2.utils.getBackgroundImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BestQuotesViewModel @Inject constructor(private val getBestQuotes: GetBestQuotes) : ViewModel() {

  private val _bestQuotes = MutableStateFlow<State<List<BestQuotesView>>>(State.LoadingState)
  val bestQuotes = _bestQuotes.asStateFlow()

  val listState = LazyListState()
  val dataLoaded = mutableStateOf(false)

  fun getBestQuotes() {
    viewModelScope.launch {
      if (dataLoaded.value) {
        return@launch
      }
      getBestQuotes.invoke(Unit).collectLatest {
        when (it) {
          is State.LoadingState, is State.ErrorState -> _bestQuotes.emit(it)
          is State.DataState -> {
            val data = it.data.mapIndexed { index, bestQuoteView ->
              bestQuoteView.copy(
                quoteCategoryView = bestQuoteView.quoteCategoryView.copy(
                  resId = bestQuoteView.quoteCategoryView.getBackgroundImage()
                )
              )
            }
            dataLoaded.value = true
            _bestQuotes.emit(
              State.DataState(
                data
              )
            )
          }
        }
      }
    }
  }
}