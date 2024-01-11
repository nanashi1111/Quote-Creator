package com.klmobile.quotescreatorv2.screens.bestquotes

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.klmobile.domain.State
import com.klmobile.quotescreatorv2.R
import com.klmobile.quotescreatorv2.Screen
import com.klmobile.quotescreatorv2.screens.quotes.bestquotes.components.BestQuoteList
import com.klmobile.quotescreatorv2.screens.common.AppToolbar
import com.klmobile.quotescreatorv2.screens.quotes.QuoteSharedViewModel
import com.klmobile.quotescreatorv2.screens.quotes.bestquotes.BestQuotesViewModel
import com.klmobile.quotescreatorv2.ui.theme.colorFromHex


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestQuotesScreen(
  navigationController: NavHostController,
  sharedViewModel: QuoteSharedViewModel,
  bestQuotesViewModel: BestQuotesViewModel,
) {
  LaunchedEffect(key1 = Unit) {
    bestQuotesViewModel.getBestQuotes()
  }
  val bestQuotesState = bestQuotesViewModel.bestQuotes.collectAsState()
  Scaffold(
    modifier = Modifier
      .fillMaxSize(),
    topBar = {
      AppToolbar(title = stringResource(id = R.string.categories)) {
        navigationController.navigateUp()
      }
    },
    containerColor = colorFromHex("1d233a")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(it)
    ) {

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        when (bestQuotesState.value) {
          is State.LoadingState -> {
            CircularProgressIndicator()
          }

          is State.DataState -> {
            BestQuoteList(
              modifier = Modifier.fillMaxSize(),
              state = bestQuotesViewModel.listState,
              bestQuotes = (bestQuotesState.value as State.DataState).data
            ) { bestQuotesView ->
              sharedViewModel.selectCategory(bestQuotesView.quoteCategoryView)
              navigationController.navigate(Screen.SCREEN_CATEGORY_DETAIL)
            }
          }

          is State.ErrorState -> {
            Text(text = "Error", color = Color.White)
          }
        }
      }

    }
  }
}
