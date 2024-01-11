package com.klmobile.quotescreatorv2.screens.quotes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.klmobile.domain.entities.QuoteCategoryView
import javax.inject.Inject

class QuoteSharedViewModel @Inject constructor() : ViewModel() {
  var selectedCategory = mutableStateOf<QuoteCategoryView?>(null)
    private set
  fun selectCategory(quoteCategoryView: QuoteCategoryView) {
    selectedCategory.value = quoteCategoryView
  }
}