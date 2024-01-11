package com.klmobile.quotescreatorv2.screens.categorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klmobile.domain.State
import com.klmobile.domain.entities.QuoteCategoryView
import com.klmobile.domain.entities.QuoteContentView
import com.klmobile.domain.usecases.GetQuotesFromCategory
import com.klmobile.quotescreatorv2.utils.quoteBackgroundList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(private val getQuotesFromCategory: GetQuotesFromCategory) : ViewModel() {

  private val _quotes = MutableStateFlow<State<List<QuoteContentView>>>(State.LoadingState)
  val quotes = _quotes.asStateFlow()

  fun getQuotesFromCategory(quoteCategoryView: QuoteCategoryView) {
    viewModelScope.launch {
      getQuotesFromCategory.invoke(quoteCategoryView).collectLatest {
        _quotes.emit(it)
        when (it) {
          is State.LoadingState, is State.ErrorState -> _quotes.emit(it)
          is State.DataState -> {
            val backgroundResource = quoteBackgroundList.shuffled()
            val data = it.data.mapIndexed { index, quoteContentView ->
              quoteContentView.copy(resId = backgroundResource[index % backgroundResource.size])
            }
            _quotes.emit(
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