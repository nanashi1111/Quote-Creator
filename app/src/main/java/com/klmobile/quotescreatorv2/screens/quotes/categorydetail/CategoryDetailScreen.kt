package com.klmobile.quotescreatorv2.screens.quotes.categorydetail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import com.klmobile.domain.entities.QuoteContentView
import com.klmobile.quotescreatorv2.screens.categorydetail.CategoryDetailViewModel
import com.klmobile.quotescreatorv2.screens.common.AppToolbar
import com.klmobile.quotescreatorv2.screens.quotes.QuoteSharedViewModel
import com.klmobile.quotescreatorv2.screens.quotes.categorydetail.components.QuoteList
import com.klmobile.quotescreatorv2.ui.theme.colorFromHex
import com.klmobile.quotescreatorv2.utils.toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
  navHostController: NavHostController,
  quoteSharedViewModel: QuoteSharedViewModel,
  categoryDetailViewModel: CategoryDetailViewModel
) {
  val quotes = categoryDetailViewModel.quotes.collectAsState()
  val context = LocalContext.current
  DisposableEffect(key1 = quoteSharedViewModel.selectedCategory) {
    quoteSharedViewModel.selectedCategory.value?.let { categoryDetailViewModel.getQuotesFromCategory(it) }

    onDispose {
      Log.d("DisposableEffect", "Disposed")
    }
  }

  Scaffold(
    topBar = {
      AppToolbar(
        title = quoteSharedViewModel.selectedCategory.value?.categoryName ?: "",
        onBackClick = {
          navHostController.navigateUp()
        },
      )
    },
    containerColor = colorFromHex("1d233a")
  ) {
    Column(
      Modifier
        .fillMaxSize()
        .padding(it),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      when (quotes.value) {
        is com.klmobile.domain.State.LoadingState -> CircularProgressIndicator()
        is com.klmobile.domain.State.DataState -> QuoteList(modifier = Modifier.fillMaxSize(), quotes = (quotes.value as com.klmobile.domain.State.DataState<List<QuoteContentView>>).data)
        is com.klmobile.domain.State.ErrorState -> Text(text = "Error", color = Color.White)
      }
    }
  }
}