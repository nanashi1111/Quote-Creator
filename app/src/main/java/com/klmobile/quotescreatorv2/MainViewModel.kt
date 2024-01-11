package com.klmobile.quotescreatorv2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klmobile.domain.State
import com.klmobile.domain.usecases.PrepareQuotesIfNeeded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val prepareQuotesIfNeeded: PrepareQuotesIfNeeded
) : ViewModel() {

  val normalFlow = flow<Int> {
    var index = 100
    while (index > 0) {
      emit(index)
      index--
      delay(1000)
    }
  }.stateIn(viewModelScope, SharingStarted.Lazily, 1)

  val _stateFlow = MutableSharedFlow<Int>()

  private fun provideInt() : Int {
    Log.d("Test", "ProvidingInt")
    return 4
  }


  fun prepareQuotesIfNeeded() = viewModelScope.launch {
    prepareQuotesIfNeeded.invoke(Unit).collect()
  }

  fun test() {
    viewModelScope.launch {
      delay(1000)
      _stateFlow.collectLatest {
        Log.d("Test", "Value = $it")
      }
    }
  }
}